package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private Integer id;
    private User user;
    private Date time;

    @Id
    @GeneratedValue(generator = "incLog")
    @GenericGenerator(name = "incLog", strategy = "native")
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

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
