package com.cirnoteam.accountingassistant.web.json;

import java.util.List;

/**
 * Created by Yifan on 2017/7/23.
 */
public class RecordReqEntity {
    private String token;
    private String uuid;
    private String type;
    private List<SyncRecord> records;

    public String getToken() {
        return token;
    }

    public RecordReqEntity setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public RecordReqEntity setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getType() {
        return type;
    }

    public RecordReqEntity setType(String type) {
        this.type = type;
        return this;
    }

    public List<SyncRecord> getRecords() {
        return records;
    }

    public RecordReqEntity setRecords(List<SyncRecord> records) {
        this.records = records;
        return this;
    }
}
