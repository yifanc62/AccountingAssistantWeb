package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Log
 * 数据库登录记录表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "log", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Log {
    private Long id;
    private User user;
    private Date time;

    @Id
    @GeneratedValue(generator = "incLog")
    @GenericGenerator(name = "incLog", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTime() {
        return time;
    }

    public Log setId(Long id) {
        this.id = id;
        return this;
    }

    public Log setUser(User user) {
        this.user = user;
        return this;
    }

    public Log setTime(Date time) {
        this.time = time;
        return this;
    }
}
