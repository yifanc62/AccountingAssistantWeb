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
    private Integer id;
    private Device device;
    private Integer rid;
    private Integer type;
    private Boolean deleted;
    private Date time;

    @Id
    @GeneratedValue(generator = "incDirty")
    @GenericGenerator(name = "incDirty", strategy = "native")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uuid", nullable = false)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Column(name = "rid", nullable = false)
    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
