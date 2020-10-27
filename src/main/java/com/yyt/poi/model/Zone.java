package com.yyt.poi.model;

import com.yyt.poi.annotation.ExcelField;

public class Zone {
    

    @ExcelField(name = "分区名")
    private String zoneName;

    @ExcelField(name = "分区类型")
    private String zoneType;

    @ExcelField(name = "默认分区")
    private String isDefault;

    @ExcelField(name = "分区范围")
    private String zoneRanges;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getZoneRanges() {
        return zoneRanges;
    }

    public void setZoneRanges(String zoneRanges) {
        this.zoneRanges = zoneRanges;
    }
}
