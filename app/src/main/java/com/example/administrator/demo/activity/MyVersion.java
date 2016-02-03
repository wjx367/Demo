package com.example.administrator.demo.activity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MyVersion {

    /**
     * dataList : [{"version":"1.0.0","downloadUrl":"http%3A%2F%2F183.129.157.218%3A8297%2Fecai%2Fdata%2Fappdownload%2Fecai.apk"}]
     * code : 1
     */

    private int code;
    /**
     * version : 1.0.0
     * downloadUrl : http%3A%2F%2F183.129.157.218%3A8297%2Fecai%2Fdata%2Fappdownload%2Fecai.apk
     */

    private List<DataListEntity> dataList;

    public void setCode(int code) {
        this.code = code;
    }

    public void setDataList(List<DataListEntity> dataList) {
        this.dataList = dataList;
    }

    public int getCode() {
        return code;
    }

    public List<DataListEntity> getDataList() {
        return dataList;
    }

    public static class DataListEntity {
        private String version;
        private String downloadUrl;

        public void setVersion(String version) {
            this.version = version;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getVersion() {
            return version;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }
}
