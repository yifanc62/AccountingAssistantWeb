package com.cirnoteam.accountingassistant.web.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Device
 * 数据库设备表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "device", uniqueConstraints = {@UniqueConstraint(columnNames = "uuid")})
public class Device {
    private String uuid;
    private User user;
    private String name;
    private String token;
    private Date tokenTime;
    private Date syncTime;
    private Set<Dirty> dirtySet = new HashSet<>();

    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    public String getUuid() {
        return uuid;
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

    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    @Column(name = "tokenTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTokenTime() {
        return tokenTime;
    }

    @Column(name = "syncTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getSyncTime() {
        return syncTime;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device")
    public Set<Dirty> getDirtySet() {
        return dirtySet;
    }

    public Device setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Device setUser(User user) {
        this.user = user;
        return this;
    }

    public Device setName(String name) {
        this.name = name;
        return this;
    }

    public Device setToken(String token) {
        this.token = token;
        return this;
    }

    public Device setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
        return this;
    }

    public Device setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
        return this;
    }

    public Device setDirtySet(Set<Dirty> dirtySet) {
        this.dirtySet = dirtySet;
        return this;
    }
}
