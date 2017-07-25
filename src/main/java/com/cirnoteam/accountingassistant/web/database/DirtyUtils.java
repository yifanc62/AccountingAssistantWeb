package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.*;
import com.cirnoteam.accountingassistant.web.json.SyncAccount;
import com.cirnoteam.accountingassistant.web.json.SyncBook;
import com.cirnoteam.accountingassistant.web.json.SyncRecord;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yifan on 2017/7/21.
 */
public class DirtyUtils {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();
    private static int TYPE_BOOK = 2;
    private static int TYPE_ACCOUNT = 1;
    private static int TYPE_RECORD = 0;

    private static void insertDirty(Session session, Dirty dirty) throws DbException {
        try {
            session.getTransaction().begin();
            session.persist(dirty);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入脏数据失败！");
        }
    }

    private static void updateDirty(Session session, Dirty dirty) throws DbException {
        try {
            session.getTransaction().begin();
            session.update(dirty);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("更新脏数据失败！");
        }
    }

    private static void deleteDirty(Session session, Dirty dirty) throws DbException {
        try {
            session.getTransaction().begin();
            session.delete(dirty);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("删除脏数据失败！");
        }
    }

    private static Dirty getDirty(Session session, Long id) throws DbException {
        try {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.equal(dirtyRoot.get("id"), id));
            Dirty dirty = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return dirty;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取脏数据失败！");
        }
    }

    private static Dirty getDirty(Session session, String uuid, Long rid, Integer type) throws DbException {
        try {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("rid"), rid), builder.equal(dirtyRoot.get("type"), type)));
            Dirty dirty = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return dirty;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取脏数据失败！");
        }
    }

    public static void deleteAllDirties(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.equal(dirtyRoot.get("uuid"), uuid));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            for (Dirty dirty : dirties) {
                deleteDirty(session, dirty);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取脏数据失败！");
        }
    }

    public static void addAllUuidDirty(String uuid, Long rid, Integer type, Boolean deleted, Date time) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            Device device = UserUtils.getDevice(uuid);
            List<String> uuids = UserUtils.findOtherUuids(uuid);
            for (String u : uuids) {
                Device d = new Device().setUuid(u);
                Dirty old = getDirty(session, uuid, rid, type);
                if (old == null) {
                    insertDirty(session, new Dirty().setDevice(d).setRid(rid).setType(type).setDeleted(deleted).setTime(time));
                } else {
                    if (old.getTime().before(time)) {
                        old.setDeleted(deleted).setTime(time);
                        updateDirty(session, old);
                    }
                }
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncBook> getAllNonDeleteBooks(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_BOOK), builder.equal(dirtyRoot.get("deleted"), false)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncBook> result = new ArrayList<>();
            Session bookSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Book book = BookUtils.getBook(bookSession, dirty.getRid());
                result.add(new SyncBook().setRemoteId(book.getId()).setName(book.getName()).setUsername(book.getUser().getUsername()).setTime(dirty.getTime()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncBook> getAllDeleteBooks(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_BOOK), builder.equal(dirtyRoot.get("deleted"), true)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncBook> result = new ArrayList<>();
            Session bookSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Book book = BookUtils.getBook(bookSession, dirty.getRid());
                result.add(new SyncBook().setRemoteId(book.getId()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncAccount> getAllNonDeleteAccounts(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_ACCOUNT), builder.equal(dirtyRoot.get("deleted"), false)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncAccount> result = new ArrayList<>();
            Session accountSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Account account = AccountUtils.getAccount(accountSession, dirty.getRid());
                result.add(new SyncAccount().setRemoteId(account.getId()).setType(account.getType()).setBalance(account.getBalance()).setName(account.getName()).setRemoteBookId(account.getBook().getId()).setTime(dirty.getTime()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncAccount> getAllDeleteAccounts(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_ACCOUNT), builder.equal(dirtyRoot.get("deleted"), true)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncAccount> result = new ArrayList<>();
            Session accountSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Account account = AccountUtils.getAccount(accountSession, dirty.getRid());
                result.add(new SyncAccount().setRemoteId(account.getId()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncRecord> getAllNonDeleteRecords(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_RECORD), builder.equal(dirtyRoot.get("deleted"), false)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncRecord> result = new ArrayList<>();
            Session recordSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Record record = RecordUtils.getRecord(recordSession, dirty.getRid());
                result.add(new SyncRecord().setRemoteId(record.getId()).setAmount(record.getAmount()).setExpense(record.getExpense()).setRemark(record.getRemark()).setType(record.getType()).setRemoteAccountId(record.getAccount().getId()).setrTime(record.getTime()).setTime(dirty.getTime()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }

    public static List<SyncRecord> getAllDeleteRecords(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Dirty> criteria = builder.createQuery(Dirty.class);
            Root<Dirty> dirtyRoot = criteria.from(Dirty.class);
            criteria.select(dirtyRoot).where(builder.and(builder.equal(dirtyRoot.get("uuid"), uuid), builder.equal(dirtyRoot.get("type"), TYPE_RECORD), builder.equal(dirtyRoot.get("deleted"), true)));
            List<Dirty> dirties = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            List<SyncRecord> result = new ArrayList<>();
            Session recordSession = factory.openSession();
            for (Dirty dirty : dirties) {
                Record record = RecordUtils.getRecord(recordSession, dirty.getRid());
                result.add(new SyncRecord().setRemoteId(record.getId()));
            }
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("处理脏数据失败！");
        }
    }
}

