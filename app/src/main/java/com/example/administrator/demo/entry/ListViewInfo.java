package com.example.administrator.demo.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ListViewInfo implements Serializable {
    private int totalPage;
    private int code;
    private String msg;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    /*
     "name": "ios流程1214",   String
     "yield": 4,        Double
     "process": "100",   integer
     */
    private List<ListViewInfoEntity> dataList;

    public List<ListViewInfoEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<ListViewInfoEntity> dataList) {
        this.dataList = dataList;
    }

    public static class  ListViewInfoEntity implements Serializable{
        private  String name;
        private  double yield;
        private  int process;

        public String getName(String name) {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getYield(String yield) {
            return this.yield;
        }

        public void setYield(double yield) {
            this.yield = yield;
        }

        public int getProcess(String process) {
            return this.process;
        }

        public void setProcess(int process) {
            this.process = process;
        }
    }

}
