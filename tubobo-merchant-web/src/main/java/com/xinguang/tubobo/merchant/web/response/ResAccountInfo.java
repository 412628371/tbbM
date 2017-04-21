package com.xinguang.tubobo.merchant.web.response;

public class ResAccountInfo {

    private long balance;
    private long frozen;
    private long deposit;

    private String bankCard;
    private String bankName;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getFrozen() {
        return frozen;
    }

    public void setFrozen(long frozen) {
        this.frozen = frozen;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
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
