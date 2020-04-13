package com.atlp.netty.common;

public class Header {

    private Integer crcCode;
    private Integer len;
    private Short station;
    private String source;
    private String destination;
    private Short component;
    private Byte type;

    public Integer getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(Integer crcCode) {
        this.crcCode = crcCode;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public Short getStation() {
        return station;
    }

    public void setStation(Short station) {
        this.station = station;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Short getComponent() {
        return component;
    }

    public void setComponent(Short component) {
        this.component = component;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
