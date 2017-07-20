package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Record
 * 数据库流水表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "record", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Record {
    private Integer id;
    private Account account;
    private Boolean expense;
    private Float amount;
    private String remark;
    private Integer type;
    private Date time;

    @Id
    @GeneratedValue(generator = "incRecord")
    @GenericGenerator(name = "incRecord", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountid", nullable = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Column(name = "expense", nullable = false)
    public Boolean getExpense() {
        return expense;
    }

    public void setExpense(Boolean expense) {
        this.expense = expense;
    }

    @Column(name = "amount", nullable = false)
    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIME)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
