package com.an.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReader;
import net.sf.jxls.transformer.XLSTransformer;

public class FileUtil{

    public static List<Object> parseExcelFileToBeans(InputStream xlsFile, File jxlsConfigFile) throws Exception {
        XLSReader xlsReader = ReaderBuilder.buildFromXML(jxlsConfigFile);
        List<Object> result = new ArrayList<Object>();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("result", result);
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(xlsFile);
            xlsReader.read(inputStream, beans);            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (inputStream !=null){
                inputStream.close();
            }
        }        
        return result;
   }
    
    
    
    /**
	 * 下载导出的excel 表，此函数可以通用
	 * 
	 * @param beans
	 *            要导出的数据
	 * @param request
	 * @param response
	 * @param templatePath
	 *            模板文件的路径
	 * @throws Exception
	 */
	public static void downloadExcel(Map<String, Object> beans,
			HttpServletRequest request, HttpServletResponse response,
			String templatePath) throws Exception {
		String currentTime = Period.getSystemTime();
		String exportFileName = currentTime + ".xls";
		XLSTransformer transformer = new XLSTransformer();
		transformer.transformXLS(templatePath, beans, exportFileName);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(exportFileName, "UTF-8"));
		response.setContentType("application/octet-stream; charset=utf-8");
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(FileUtils.readFileToByteArray(new File(
				exportFileName)));
		outputStream.flush();
		outputStream.close();

	}
	
	/**
	 * 
	 * @param request
	 * @param configFilePath 导入的excel文件对应的配置文件路径
	 * @return list excel中的数据
	 * @throws Exception
	 */
	public static List importData(HttpServletRequest request,
			String configFilePath) throws Exception {
		InputStream is = null;
		String filename = request.getHeader("X-File-Name");
		try {
			is = request.getInputStream();
			String errorType = "xls,xlsx";
			String ext = filename.substring(filename.lastIndexOf(".") + 1);
			System.out.println(errorType.indexOf(ext));
			if (errorType.indexOf(ext) >= 0) {

				File configFile = new File(configFilePath);
				return parseExcelFileToBeans(is, configFile);
			} else {
				throw new IOException("文件格式错误！");
			}
		} finally {
			if (is != null)
				is.close();
		}

	}
	
}
