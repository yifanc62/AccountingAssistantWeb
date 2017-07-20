package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Account
 * 数据库账户表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "account", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Account {
    private Integer id;
    private Book book;
    private Integer type;
    private Float balance;
    private String name;
    private List<Record> records = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "incAccount")
    @GenericGenerator(name = "incAccount", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookid", nullable = false)
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "balance", nullable = false)
    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
