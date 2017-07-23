package com.cirnoteam.accountingassistant.web.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Account
 * 数据库脏数据表实体类
 *
 * @author Yifan
 * @version 1.0
 */

@Entity
@Table(name = "dirty", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Dirty {
    private Long id;
    private Device device;
    private Long rid;
    private Integer type;
    private Boolean deleted;
    private Date time;

    @Id
    @GeneratedValue(generator = "incDirty")
    @GenericGenerator(name = "incDirty", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uuid", nullable = false)
    public Device getDevice() {
        return device;
    }

    @Column(name = "rid", nullable = false)
    public Long getRid() {
        return rid;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    @Column(name = "deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTime() {
        return time;
    }

    public Dirty setId(Long id) {
        this.id = id;
        return this;
    }

    public Dirty setDevice(Device device) {
        this.device = device;
        return this;
    }

    public Dirty setRid(Long rid) {
        this.rid = rid;
        return this;
    }

    public Dirty setType(Integer type) {
        this.type = type;
        return this;
    }

    public Dirty setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Dirty setTime(Date time) {
        this.time = time;
        return this;
    }
}
