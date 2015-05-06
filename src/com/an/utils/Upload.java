package com.an.utils;


import com.an.sys.entity.Setting;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文件上传
 * Created by karas on 12/8/14.
 */
public class Upload {


    public static List<Map> multiUpload(HttpServletRequest request) throws IllegalStateException, IOException {
        long startTime = System.currentTimeMillis();
        String filename;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        new File(Setting.resourcePath).mkdirs();
        List<Map> list = new ArrayList<>();
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {

                    String ext = getFileExt(file.getOriginalFilename());
                    filename = Util.RunTimeSequence() + "." + ext;
                    String path = Setting.resourcePath + filename;
                    file.transferTo(new File(path));
                    Map rst = new HashMap<>();
                    rst.put("name", filename);
                    rst.put("originalName", file.getOriginalFilename());
                    rst.put("size", file.getSize() + "");
                    rst.put("url", filename);
                    rst.put("state", "SUCCESS");
                    rst.put("type", "." + ext);
                    list.add(rst);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("上传时间：" + String.valueOf(endTime - startTime) + "ms");
        return list;
    }

    public static String htmlUpload(HttpServletRequest request, String path) throws IllegalStateException, IOException {
        FileOutputStream fos;
        InputStream is = null;
        String filename = request.getHeader("X-File-Name");
        try {
            is = request.getInputStream();
            String errorType = "jpeg,png,gif,jpg";
            String ext = getFileExt(filename);
            if (errorType.indexOf(ext) > 0) {
                new File(path).mkdirs();

                filename = Util.RunTimeSequence() + "." + ext;
                fos = new FileOutputStream(new File(Setting.resourcePath + filename));
                IOUtils.copy(is, fos);
            } else {
                throw new IOException("图片格式错误！");
            }
        } finally {
            if (is != null)
                is.close();
        }

        return filename;
    }

    private static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
