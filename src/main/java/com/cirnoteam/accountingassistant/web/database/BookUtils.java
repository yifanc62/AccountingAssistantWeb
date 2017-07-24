package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.Book;
import com.cirnoteam.accountingassistant.web.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

    public static Long addBook(String name, String username) throws DbException {
        Book book = new Book().setName(name).setUser(new User().setUsername(username));
        insertBook(factory.openSession(), book);
        return book.getId();
    }

    public static void removeBook(Long id) throws DbException {
        Book book = new Book().setId(id);
        deleteBook(factory.openSession(), book);
    }
}
