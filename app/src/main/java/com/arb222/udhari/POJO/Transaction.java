package com.arb222.udhari.POJO;

public class Transaction {
    int initiatedBy;
    double amount,paidBy1,paidBy2,paidFor1,paidFor2;
    long timestamp;
    String desc,reference,deleteOther,transactionId;

    public Transaction(int initiatedBy, double amount, double paidBy1
            , double paidBy2, double paidFor1
            , double paidFor2, long timestamp
            , String desc, String reference
            , String deleteOther, String transactionId) {
        this.initiatedBy = initiatedBy;
        this.amount = amount;
        this.paidBy1 = paidBy1;
        this.paidBy2 = paidBy2;
        this.paidFor1 = paidFor1;
        this.paidFor2 = paidFor2;
        this.timestamp = timestamp;
        this.desc = desc;
        this.reference = reference;
        this.deleteOther = deleteOther;
        this.transactionId = transactionId;
    }

    public int getInitiatedBy() {
        return initiatedBy;
    }

    public double getAmount() {
        return amount;
    }

    public double getPaidBy1() {
        return paidBy1;
    }

    public double getPaidBy2() {
        return paidBy2;
    }

    public double getPaidFor1() {
        return paidFor1;
    }

    public double getPaidFor2() {
        return paidFor2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDesc() {
        return desc;
    }

    public String getReference() {
        return reference;
    }

    public String getDeleteOther() {
        return deleteOther;
    }

    public Transaction() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
