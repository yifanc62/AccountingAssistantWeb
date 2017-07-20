package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
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
    private Integer id;
    private User user;
    private String name;
    private List<Account> accounts = new ArrayList<>();

    @Id
    @GeneratedValue(generator = "incBook")
    @GenericGenerator(name = "incBook", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


}
