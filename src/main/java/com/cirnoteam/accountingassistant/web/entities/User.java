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

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", length = 32, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "activateToken")
    public String getActivateToken() {
        return activateToken;
    }

    public void setActivateToken(String activateToken) {
        this.activateToken = activateToken;
    }

    @Column(name = "activateCode")
    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    @Column(name = "activateTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    @Column(name = "resetToken")
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Column(name = "resetCode")
    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    @Column(name = "resetTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<Log> getLogs() {
        return logs;
    }

    public void setLogs(Set<Log> logs) {
        this.logs = logs;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
