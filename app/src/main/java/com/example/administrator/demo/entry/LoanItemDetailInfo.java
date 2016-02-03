package com.example.administrator.demo.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/14.
 */
public class LoanItemDetailInfo implements Serializable{
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

    private List<LoanItemDetailEntity> dataList;

    public List<LoanItemDetailEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<LoanItemDetailEntity> dataList) {
        this.dataList = dataList;
    }

    public static class LoanItemDetailEntity implements Serializable{
        private int id;
        private String name;
        private int borrowStatus;
        private double yield;
        private int period;
        private String process;
        private double entryUnit;
        private double maxUnit;
        private String borrowStyle;
        private double accountYes;
        private double account;
        private String borrowStatusStr;
        private int isday;
        private int timeLimitDay;
        private String timeLimit;
        private String profitTime;
        private double amount;
        private int createdTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBorrowStatus() {
            return borrowStatus;
        }

        public void setBorrowStatus(int borrowStatus) {
            this.borrowStatus = borrowStatus;
        }

        public double getYield() {
            return yield;
        }

        public void setYield(double yield) {
            this.yield = yield;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public double getEntryUnit() {
            return entryUnit;
        }

        public void setEntryUnit(double entryUnit) {
            this.entryUnit = entryUnit;
        }

        public double getMaxUnit() {
            return maxUnit;
        }

        public void setMaxUnit(double maxUnit) {
            this.maxUnit = maxUnit;
        }

        public String getBorrowStyle() {
            return borrowStyle;
        }

        public void setBorrowStyle(String borrowStyle) {
            this.borrowStyle = borrowStyle;
        }

        public double getAccountYes() {
            return accountYes;
        }

        public void setAccountYes(double accountYes) {
            this.accountYes = accountYes;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public String getBorrowStatusStr() {
            return borrowStatusStr;
        }

        public void setBorrowStatusStr(String borrowStatusStr) {
            this.borrowStatusStr = borrowStatusStr;
        }

        public int getIsday() {
            return isday;
        }

        public void setIsday(int isday) {
            this.isday = isday;
        }

        public int getTimeLimitDay() {
            return timeLimitDay;
        }

        public void setTimeLimitDay(int timeLimitDay) {
            this.timeLimitDay = timeLimitDay;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getProfitTime() {
            return profitTime;
        }

        public void setProfitTime(String profitTime) {
            this.profitTime = profitTime;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(int createdTime) {
            this.createdTime = createdTime;
        }
    }
}
