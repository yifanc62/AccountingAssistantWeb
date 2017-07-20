package com.cirnoteam.accountingassistant.web.entities;

import javax.persistence.*;
import java.sql.Timestamp;
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "tokenTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Column(name = "syncTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device")
    public Set<Dirty> getDirtySet() {
        return dirtySet;
    }

    public void setDirtySet(Set<Dirty> dirtySet) {
        this.dirtySet = dirtySet;
    }
}
