package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.Account;
import com.cirnoteam.accountingassistant.web.entities.Record;
import com.cirnoteam.accountingassistant.web.json.SyncRecord;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by Yifan on 2017/7/21.
 */
public class RecordUtils {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    private static void insertRecord(Session session, Record record) throws DbException {
        try {
            session.getTransaction().begin();
            session.persist(record);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入流水失败！");
        }
    }

    private static void updateRecord(Session session, Record record) throws DbException {
        try {
            session.getTransaction().begin();
            session.update(record);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("更新流水失败！");
        }
    }

    private static void deleteRecord(Session session, Record record) throws DbException {
        try {
            session.getTransaction().begin();
            session.delete(record);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("删除流水失败！");
        }
    }

    public static Record getRecord(Session session, Long id) throws DbException {
        try {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Record> criteria = builder.createQuery(Record.class);
            Root<Record> recordRoot = criteria.from(Record.class);
            criteria.select(recordRoot).where(builder.equal(recordRoot.get("id"), id));
            Record record = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return record;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取流水失败！");
        }
    }

    public static Long addRecord(SyncRecord syncRecord) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            Record record = new Record().setAccount(new Account().setId(syncRecord.getRemoteAccountId())).setAmount(syncRecord.getAmount()).setExpense(syncRecord.getExpense()).setRemark(syncRecord.getRemark()).setType(syncRecord.getType()).setTime(syncRecord.getrTime());
            insertRecord(session, record);
            return record.getId();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("同步流水失败！");
        }
    }
}
