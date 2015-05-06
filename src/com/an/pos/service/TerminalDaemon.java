package com.an.pos.service;

import com.an.sys.entity.Setting;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.ag.api.ApiLog;
//import com.ag.api.dao.ApiLogDao;

@Service
public class TerminalDaemon {
    private static final Logger logger = LoggerFactory
            .getLogger(TerminalDaemon.class);


    private String orgCode;
    private String terminalCode;
    private String terminalType;
    //private ApiLogDao logDao;
    private String tPath;

    /*
     * 扫描已完成上传文件
     */
    public void push() {
        BufferedReader br = null;

        try {
            //logDao = new ApiLogDao(this.sqlSession);
            File dir = new File(Setting.ftpRoot + "download/");
            File stores[] = dir.listFiles();
            for (File store : stores) {
                if (!store.isDirectory()) {
                    continue;
                }
                this.orgCode = store.getName();
                File terminals[] = store.listFiles();
                for (File terminal : terminals) {
                    if (!terminal.isDirectory()) {
                        continue;
                    }
                    terminalCode = terminal.getName();
                    tPath = terminal.getAbsolutePath();
                    File rm60cmd = new File(tPath + "/rm60cmd.txt");
                    if (rm60cmd.exists()) {

                        File rm60err = new File(tPath + "/rm60err.txt");
                        boolean errFlg = rm60err.exists();

                        List<String> files = new ArrayList<String>();
                        if (rm60cmd.length() > 0) {
                            br = new BufferedReader(new FileReader(rm60cmd));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                files.add(line);
                            }
                            br.close();
                        }

                        OutputStreamWriter writer = new OutputStreamWriter(
                                new FileOutputStream(rm60cmd), "GBK");
                        for (String name : files) {
                            File dataFile = new File(tPath + "/" + name);
                            if (dataFile.exists()) {
                                if (errFlg) {
                                    this.error(dataFile, "报文下载处理失败!");
                                } else {
                                    writer.write(name + "\r\n");
                                }
                            } else {
                                dataFile = new File(tPath
                                        + "/"
                                        + name.substring(0,
                                        name.lastIndexOf(".")) + ".uok");
                                this.success(dataFile);
                            }
                        }
                        writer.flush();
                        writer.close();

                        if (errFlg) {
                            rm60err.renameTo(new File(tPath + "/wrong_data/"
                                    + Util.StampFmt.format(new Date()) + ".err"));
                        }
                    } else {
                        File fs[] = terminal.listFiles();
                        for (File file : fs) {
                            if (file.getName().endsWith(".uok")) {
                                file.delete();
                                new File(file.getAbsolutePath().replace(".uok", ".fok")).delete();
                                new File(file.getAbsolutePath().replace(".uok", ".dok")).delete();
                                this.success(file);
                            } else if (file.getName().endsWith(".uer")) {
                                file.delete();
                                new File(file.getAbsolutePath().replace(".uok", ".fok")).delete();
                                new File(file.getAbsolutePath().replace(".uok", ".dok")).delete();
                                this.error(new File(file.getAbsolutePath().replace(".uok", ".zip")), "报文下载处理失败!");
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void error(File file, String e) {
        File dest = new File(file.getAbsolutePath().replace(file.getName(),
                "wrong_data/" + file.getName()));
        file.renameTo(dest);
        log(file, "error", e);
    }

    private void success(File file) {
        File dest = new File(file.getAbsolutePath().replace(file.getName(),
                "backup/" + file.getName()));
        file.renameTo(dest);
        log(file, "success", "报文下载成功");
    }

    private void log(File file, String status, String remark) {
//        ApiLog log = new ApiLog();
//        log.setDeviceCode(terminalType + ":" + this.terminalCode);
//        log.setOrgCode(this.orgCode);
//        log.setInOut("in");
//        log.setSendTimestamp(Util.StampFmt.format(new Date(file.lastModified())));
//        log.setReceiptTimestamp(Util.StampFmt.format(new Date()));
//        log.setKeyword(file.getName());
//        log.setRemark(remark);
//        log.setSerialNo(String.valueOf(logDao.nextSerail()));
//        log.setStatus(status);
//        logger.info(status + remark + ":" + file.getAbsolutePath());
//        logDao.save(log);
    }
}
