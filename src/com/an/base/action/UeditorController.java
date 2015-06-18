package com.an.base.action;

import com.an.sys.entity.Setting;
import com.an.utils.Upload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.*;

@Controller
@RequestMapping("/ueditor")
public class UeditorController {
	private ObjectMapper jsonObjectMapper = new ObjectMapper();

	@RequestMapping("/getContent")
	public String getContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "";
	}

	@RequestMapping("/getMovie")
	public String getMovie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";
		String searchkey = request.getParameter("searchKey");
		String videotype = request.getParameter("videoType");
		try {
			searchkey = URLEncoder.encode(searchkey, "utf-8");
			URL url = new URL(
					"http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw="
							+ searchkey + "&pageNo=1&pageSize=20&channelId="
							+ videotype + "&inDays=7&media=v&sort=s");
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				readOneLineBuff.append(line);
			}
			content = readOneLineBuff.toString();
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		response.getWriter().print(content);
		return "";
	}

	@RequestMapping("/getRemoteImage")
	public String getRemoteImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String url = request.getParameter("upfile");
		String state = "远程图片抓取成功！";

		String filePath = "upload";
		String[] arr = url.split("ue_separate_ue");
		String[] outSrc = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {

			// 保存文件路径
			String str = Setting.resourcePath;
			File f = new File(str);
			String savePath = f.getParent() + "/" + filePath;
			// 格式验证
			String type = getFileType(arr[i]);
			if (type.equals("")) {
				state = "图片类型不正确！";
				continue;
			}
			String saveName = "d_" + Long.toString(new Date().getTime()) + type;
			// 大小验证
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection conn = (HttpURLConnection) new URL(arr[i])
					.openConnection();
			if (conn.getContentType().indexOf("image") == -1) {
				state = "请求地址头不正确";
				continue;
			}
			if (conn.getResponseCode() != 200) {
				state = "请求地址不存在！";
				continue;
			}
			File dir = new File(savePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File savetoFile = new File(savePath + "/" + saveName);
			outSrc[i] = filePath + "/" + saveName;
			try {
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(savetoFile);
				int b;
				while ((b = is.read()) != -1) {
					os.write(b);
				}
				os.close();
				is.close();
				// 这里处理 inputStream
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("页面无法访问");
			}
		}
		String outstr = "";
		for (int i = 0; i < outSrc.length; i++) {
			outstr += outSrc[i] + "ue_separate_ue";
		}
		outstr = outstr.substring(0, outstr.lastIndexOf("ue_separate_ue"));
		response.getWriter().print("{'url':'" + outstr + "','tip':'" + state + "','srcUrl':'" + url + "'}");
		return "";
	}

	/**
	 * 富文本编辑器上传图片
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/imageUp")
	@ResponseBody
	public String imageUp(HttpServletRequest request) throws Exception {
		List<Map<String, String>> files = Upload.multiUpload(request);
		return jsonObjectMapper.writeValueAsString(files.get(0));
	}

	public String getFileType(String fileName) {
		String[] fileType = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
		Iterator<String> type = Arrays.asList(fileType).iterator();
		while (type.hasNext()) {
			String t = type.next();
			if (fileName.endsWith(t)) {
				return t;
			}
		}
		return "";
	}

}
