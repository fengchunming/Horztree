package com.an.pos.entity;

import com.an.sys.entity.Organization;

import java.util.Date;

/**
 * 门店终端（秤) 主数据
 *
 * @author Karas
 */
public class Terminal {
    private Integer terminalId = 0; // 终端ID
    private String terminalCode; // 终端编号
    private String terminalType; // 终端类型
    private Organization store; // 门店
    private String ipAddress; // ip地址
    private String portNo; // 端口号
    private String connectionType; // 数据传输方式
    private String status; // 状态
    private Date timestamp;

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Organization getStore() {
        return store;
    }

    public void setStore(Organization store) {
        this.store = store;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
