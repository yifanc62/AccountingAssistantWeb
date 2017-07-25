package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.Account;
import com.cirnoteam.accountingassistant.web.entities.Book;
import com.cirnoteam.accountingassistant.web.json.SyncAccount;
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
public class AccountUtils {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    private static void insertAccount(Session session, Account account) throws DbException {
        try {
            session.getTransaction().begin();
            session.persist(account);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入账户失败！");
        }
    }

    private static void updateAccount(Session session, Account account) throws DbException {
        try {
            session.getTransaction().begin();
            session.update(account);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("更新账户失败！");
        }
    }

    private static void deleteAccount(Session session, Account account) throws DbException {
        try {
            session.getTransaction().begin();
            session.delete(account);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("删除账户失败！");
        }
    }

    private static Account getAccount(Long id) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Account> criteria = builder.createQuery(Account.class);
            Root<Account> accountRoot = criteria.from(Account.class);
            criteria.select(accountRoot).where(builder.equal(accountRoot.get("id"), id));
            Account account = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return account;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取账户失败！");
        }
    }

    public static Long addAccount(SyncAccount account) throws DbException {
        Account newAccount = new Account().setName(account.getName()).setBook(new Book().setId(account.getRemoteBookId())).setType(account.getType()).setBalance(account.getBalance());
        insertAccount(factory.openSession(), newAccount);
        return newAccount.getId();
    }
}
