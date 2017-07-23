package com.cirnoteam.accountingassistant.web.entities;

import javax.persistence.*;
import java.util.*;

/**
 * User
 * 数据库用户表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String activateToken;
    private String activateCode;
    private Date activateTime;
    private String resetToken;
    private String resetCode;
    private Date resetTime;
    private Set<Device> devices = new HashSet<>();
    private Set<Log> logs = new HashSet<>();
    private List<Book> books = new ArrayList<>();

    @Id
    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    @Column(name = "password", length = 32, nullable = false)
    public String getPassword() {
        return password;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    @Column(name = "activateToken")
    public String getActivateToken() {
        return activateToken;
    }

    @Column(name = "activateCode")
    public String getActivateCode() {
        return activateCode;
    }

    @Column(name = "activateTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getActivateTime() {
        return activateTime;
    }

    @Column(name = "resetToken")
    public String getResetToken() {
        return resetToken;
    }

    @Column(name = "resetCode")
    public String getResetCode() {
        return resetCode;
    }

    @Column(name = "resetTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getResetTime() {
        return resetTime;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<Device> getDevices() {
        return devices;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<Log> getLogs() {
        return logs;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public List<Book> getBooks() {
        return books;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public User setActivateToken(String activateToken) {
        this.activateToken = activateToken;
        return this;
    }

    public User setActivateCode(String activateCode) {
        this.activateCode = activateCode;
        return this;
    }

    public User setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
        return this;
    }

    public User setResetToken(String resetToken) {
        this.resetToken = resetToken;
        return this;
    }

    public User setResetCode(String resetCode) {
        this.resetCode = resetCode;
        return this;
    }

    public User setResetTime(Date resetTime) {
        this.resetTime = resetTime;
        return this;
    }

    public User setDevices(Set<Device> devices) {
        this.devices = devices;
        return this;
    }

    public User setLogs(Set<Log> logs) {
        this.logs = logs;
        return this;
    }

    public User setBooks(List<Book> books) {
        this.books = books;
        return this;
    }
}
