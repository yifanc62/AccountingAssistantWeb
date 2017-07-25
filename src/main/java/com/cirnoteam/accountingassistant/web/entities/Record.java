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
    private Long id;
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
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountid", nullable = false)
    public Account getAccount() {
        return account;
    }

    @Column(name = "expense", nullable = false)
    public Boolean getExpense() {
        return expense;
    }

    @Column(name = "amount", nullable = false)
    public Float getAmount() {
        return amount;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTime() {
        return time;
    }

    public Record setId(Long id) {
        this.id = id;
        return this;
    }

    public Record setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Record setExpense(Boolean expense) {
        this.expense = expense;
        return this;
    }

    public Record setAmount(Float amount) {
        this.amount = amount;
        return this;
    }

    public Record setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Record setType(Integer type) {
        this.type = type;
        return this;
    }

    public Record setTime(Date time) {
        this.time = time;
        return this;
    }
}
