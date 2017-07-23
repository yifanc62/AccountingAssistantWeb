package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Book
 * 数据库账本表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "book", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Book {
    private Long id;
    private User user;
    private String name;
    private List<Account> accounts = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "incBook")
    @GenericGenerator(name = "incBook", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    public List<Account> getAccounts() {
        return accounts;
    }

    public Book setId(Long id) {
        this.id = id;
        return this;
    }

    public Book setUser(User user) {
        this.user = user;
        return this;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public Book setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        return this;
    }
}
