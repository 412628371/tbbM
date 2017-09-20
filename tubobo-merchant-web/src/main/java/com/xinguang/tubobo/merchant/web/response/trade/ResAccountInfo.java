package com.xinguang.tubobo.merchant.web.response.trade;

public class ResAccountInfo {

    private String  balance;
    private String frozen;
    private String deposit;

    private String bankCard;
    private String bankName;


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "ResAccountInfo{" +
                "balance=" + balance +
                ", frozen=" + frozen +
                ", deposit=" + deposit +
                ", bankCard='" + bankCard + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
