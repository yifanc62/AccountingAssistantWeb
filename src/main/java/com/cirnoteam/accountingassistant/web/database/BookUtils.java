package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.Book;
import com.cirnoteam.accountingassistant.web.entities.User;
import com.cirnoteam.accountingassistant.web.json.SyncBook;
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
public class BookUtils {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    private static void insertBook(Session session, Book book) throws DbException {
        try {
            session.getTransaction().begin();
            session.persist(book);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入账本失败！");
        }
    }

    private static void updateBook(Session session, Book book) throws DbException {
        try {
            session.getTransaction().begin();
            session.update(book);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("更新账本失败！");
        }
    }

    private static void deleteBook(Session session, Book book) throws DbException {
        try {
            session.getTransaction().begin();
            session.delete(book);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("删除账本失败！");
        }
    }

    public static Book getBook(Session session, Long id) throws DbException {
        try {
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
            Root<Book> bookRoot = criteria.from(Book.class);
            criteria.select(bookRoot).where(builder.equal(bookRoot.get("id"), id));
            Book book = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return book;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取账本失败！");
        }
    }

    public static Long addBook(String name, String username) throws DbException {
        Book book = new Book().setName(name).setUser(new User().setUsername(username));
        insertBook(factory.openSession(), book);
        return book.getId();
    }

    public static void removeBook(Long id) throws DbException {
        Book book = new Book().setId(id);
        deleteBook(factory.openSession(), book);
    }

    public static void modifyBook(SyncBook syncBook) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            Book book = getBook(session, syncBook.getRemoteId());
            book.setName(syncBook.getName());
            updateBook(session, book);
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("修改账本失败！");
        }
    }

    public static Long addBook(SyncBook syncBook) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            Book book = new Book().setUser(new User().setUsername(syncBook.getUsername())).setName(syncBook.getName());
            insertBook(session, book);
            return book.getId();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("同步账本失败！");
        }
    }
}
