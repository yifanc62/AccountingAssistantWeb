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
    private Long id;
    private Book book;
    private Integer type;
    private Float balance;
    private String name;
    private List<Record> records = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "incAccount")
    @GenericGenerator(name = "incAccount", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookid", nullable = false)
    public Book getBook() {
        return book;
    }


    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }


    @Column(name = "balance", nullable = false)
    public Float getBalance() {
        return balance;
    }


    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    public List<Record> getRecords() {
        return records;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public Account setBook(Book book) {
        this.book = book;
        return this;
    }

    public Account setType(Integer type) {
        this.type = type;
        return this;
    }

    public Account setBalance(Float balance) {
        this.balance = balance;
        return this;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public Account setRecords(List<Record> records) {
        this.records = records;
        return this;
    }
}
