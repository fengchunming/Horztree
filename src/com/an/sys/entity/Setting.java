package com.an.sys.entity;

public class Setting {
    public static String mailFrom = "test@expectsun.com";
    public static String mailHost = "smtp.exmail.qq.com";
    public static String mailPort = "25";
    public static String mailUsername = "test@expectsun.com";
    public static String mailPassword = "test010101";
    public static boolean mailAuth = true;
    public static boolean mailStarttlsEnabled = true;
    public static String resourcePath = "/alidata1/static/resource/";

    public static String ftpRoot = "~/Downloads/temp/";
    public static String ftpUser = "ftpuser";
    public static String ftpPasswd = "111";
    public static Integer ftpPort = 21;
    public static boolean ftpState = false;
    public static String ftpBackupRoot = "~/Downloads/backup/";

    public static String smsUrl = "";
    public static String smsHost = "";
    public static String smsUser = "";
    public static String smsPwd = "";

    public static Integer warningStock = 1;
    public static Integer warningExpired = 1;

    public String getFtpBackupRoot() {
        return ftpBackupRoot;
    }

    public String getFtpPasswd() {
        return ftpPasswd;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public String getFtpRoot() {
        return ftpRoot;
    }

    public boolean getFtpState() {
        return ftpState;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public boolean getMailAuth() {
        return mailAuth;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailHost() {
        return mailHost;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailPort() {
        return mailPort;
    }

    public boolean getMailStarttlsEnabled() {
        return mailStarttlsEnabled;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getSmsHost() {
        return smsHost;
    }

    public String getSmsPwd() {
        return smsPwd;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public String getSmsUser() {
        return smsUser;
    }

    public Integer getWarningExpired() {
        return warningExpired;
    }

    public Integer getWarningStock() {
        return warningStock;
    }

    public void setFtpBackupRoot(String ftpBackupRoot) {
        Setting.ftpBackupRoot = ftpBackupRoot;
    }

    public void setFtpPasswd(String ftpPasswd) {
        Setting.ftpPasswd = ftpPasswd;
    }

    public void setFtpPort(Integer ftpPort) {
        Setting.ftpPort = ftpPort;
    }

    public void setFtpRoot(String ftpRoot) {
        Setting.ftpRoot = ftpRoot;
    }

    public void setFtpState(boolean ftpState) {
        Setting.ftpState = ftpState;
    }

    public void setFtpUser(String ftpUser) {
        Setting.ftpUser = ftpUser;
    }

    public void setMailAuth(boolean mailAuth) {
        Setting.mailAuth = mailAuth;
    }

    public void setMailFrom(String mailFrom) {
        Setting.mailFrom = mailFrom;
    }

    public void setMailHost(String mailHost) {
        Setting.mailHost = mailHost;
    }

    public void setMailPassword(String mailPwd) {
        Setting.mailPassword = mailPwd;
    }

    public void setMailPort(String mailPort) {
        Setting.mailPort = mailPort;
    }

    public void setMailStarttlsEnabled(boolean mailStarttlsEnabled) {
        Setting.mailStarttlsEnabled = mailStarttlsEnabled;
    }

    public void setMailUsername(String mailUser) {
        Setting.mailUsername = mailUser;
    }

    public void setResourcePath(String resourcePath) {
        Setting.resourcePath = resourcePath;
    }

    public void setSmsHost(String smsHost) {
        Setting.smsHost = smsHost;
    }

    public void setSmsPwd(String smsPwd) {
        Setting.smsPwd = smsPwd;
    }

    public void setSmsUrl(String smsUrl) {
        Setting.smsUrl = smsUrl;
    }

    public void setSmsUser(String smsUser) {
        Setting.smsUser = smsUser;
    }

    public void setWarningExpired(Integer warningExpired) {
        Setting.warningExpired = warningExpired;
    }

    public void setWarningStock(Integer warningStock) {
        Setting.warningStock = warningStock;
    }

}
