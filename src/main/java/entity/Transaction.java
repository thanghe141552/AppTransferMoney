package entity;

import entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private int transactionId;
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private Date createdTime;
    private Date updateTime;
    private TransactionStatus activeStatus;

    public Transaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public TransactionStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(TransactionStatus activeStatus) {
        this.activeStatus = activeStatus;
    }
}
