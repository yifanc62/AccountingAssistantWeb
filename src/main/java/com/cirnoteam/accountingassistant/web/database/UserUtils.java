package com.cirnoteam.accountingassistant.web.database;

import com.cirnoteam.accountingassistant.web.entities.Device;
import com.cirnoteam.accountingassistant.web.entities.Log;
import com.cirnoteam.accountingassistant.web.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * UserUtils
 */
public class UserUtils {
    private static SessionFactory factory = new Configuration().configure().buildSessionFactory();

    private static void insertUser(Session session, User user) throws DbException {
        try {
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入用户失败！");
        }
    }

    private static void updateUser(Session session, User user) throws DbException {
        try {
            session.getTransaction().begin();
            session.update(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("更新用户失败！");
        }
    }

    private static String generateUuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }

    private static String generateVerifyCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int r = 0;
            while ((r < 50) || (r > 57 && r < 65) || (r == 73) || (r == 79) || (r > 90 && r < 97) || (r == 108) || (r == 111) || (r > 122)) {
                r = (int) (Math.random() * 73);
                r += 50;
            }
            sb.append((char) r);
        }
        return sb.toString();
    }

    private static User getUser(String username) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.equal(userRoot.get("username"), username));
            User user = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    private static boolean isUserExists(String username) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.equal(userRoot.get("username"), username));
            User user = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return user != null;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    private static boolean isEmailExists(String email) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.equal(userRoot.get("email"), email));
            User user = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return user != null;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    public static String addUser(String username, String password, String email) throws RequestException, DbException {
        if (isUserExists(username)) {
            throw new RequestException("用户名已存在！");
        }
        if (isEmailExists(email)) {
            throw new RequestException("邮箱已存在！");
        }
        if (password.length() != 32) {
            throw new RequestException("密码md5长度错误！");
        }
        String activateToken = generateUuid();
        String activateCode = generateVerifyCode();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setActivateToken(activateToken);
        user.setActivateCode(activateCode);
        user.setActivateTime(new Date());
        insertUser(factory.openSession(), user);
        return activateToken;
    }

    public static String refreshToken(String uuid) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Device> criteria = builder.createQuery(Device.class);
            Root<Device> deviceRoot = criteria.from(Device.class);
            criteria.select(deviceRoot).where(builder.equal(deviceRoot.get("uuid"), uuid));
            Device device = session.createQuery(criteria).uniqueResult();
            if (device == null) {
                throw new DbException("无效的UUID！");
            }
            String newToken = generateUuid();
            device.setToken(newToken);
            device.setTokenTime(new Date());
            session.getTransaction().commit();
            return newToken;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取设备失败！");
        }
    }

    public static boolean verifyToken(String token, String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Device> criteria = builder.createQuery(Device.class);
            Root<Device> deviceRoot = criteria.from(Device.class);
            criteria.select(deviceRoot).where(builder.and(builder.equal(deviceRoot.get("uuid"), uuid), builder.equal(deviceRoot.get("token"), token)));
            Device device = session.createQuery(criteria).uniqueResult();
            if (device == null) {
                throw new RequestException("无效的token和/或UUID！");
            }
            session.getTransaction().commit();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -15);
            if (device.getTokenTime().before(calendar.getTime())) {
                throw new RequestException("登录信息已过期，请重新填写！");
            }
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取设备失败！");
        }
    }

    public static boolean activate(String activateToken, String code) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.equal(userRoot.get("activateToken"), activateToken));
            User user = session.createQuery(criteria).uniqueResult();
            if (user == null) {
                throw new RequestException("无效的激活token！");
            }
            session.getTransaction().commit();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.MINUTE, -5);
            if (user.getActivateCode().equals(code)) {
                if (user.getActivateTime().before(calendar.getTime())) {
                    throw new RequestException("验证码已失效，请重新注册！");
                } else {
                    user.setActivateToken(null);
                    user.setActivateCode(null);
                    user.setActivateTime(new Date());
                    updateUser(session, user);
                    return true;
                }
            } else {
                throw new RequestException("验证码错误！");
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    public static boolean reset(String resetToken, String code) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.equal(userRoot.get("resetToken"), resetToken));
            User user = session.createQuery(criteria).uniqueResult();
            if (user == null) {
                throw new RequestException("无效的重置token！");
            }
            session.getTransaction().commit();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.HOUR, -1);
            if (user.getResetCode().equals(code)) {
                if (user.getResetTime().before(calendar.getTime())) {
                    throw new RequestException("验证码已失效，请重新操作！");
                } else {
                    user.setResetToken(null);
                    user.setResetCode(null);
                    user.setResetTime(new Date());
                    updateUser(session, user);
                    return true;
                }
            } else {
                throw new RequestException("验证码错误！");
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    public static String verifyResetAuth(String username, String email) throws RequestException, DbException {
        if (!isUserExists(username)) {
            throw new RequestException("用户名不存在！");
        }
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot).where(builder.and(builder.equal(userRoot.get("username"), username), builder.equal(userRoot.get("email"), email)));
            User user = session.createQuery(criteria).uniqueResult();
            if (user == null) {
                throw new RequestException("用户名与邮箱不符！");
            }
            session.getTransaction().commit();
            String resetToken = generateUuid();
            String resetCode = generateVerifyCode();
            user.setResetToken(resetToken);
            user.setResetCode(resetCode);
            user.setResetTime(new Date());
            updateUser(session, user);
            return resetToken;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取用户失败！");
        }
    }

    public static void resetPassword(String username, String newPassword) throws RequestException, DbException {
        if (newPassword.length() != 32) {
            throw new RequestException("密码md5长度错误！");
        }
        User user = getUser(username);
        user.setPassword(newPassword);
        updateUser(factory.openSession(), user);
    }

    public static String addDevice(String username, String uuid, String deviceName) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            User user = getUser(username);
            Device device = new Device();
            String token = generateUuid();
            device.setUser(user);
            device.setUuid(uuid);
            device.setToken(token);
            device.setTokenTime(new Date());
            device.setSyncTime(new Date());
            device.setName(deviceName);
            session.persist(device);
            session.getTransaction().commit();
            return token;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入设备失败！");
        }
    }

    public static Device getDevice(String uuid) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Device> criteria = builder.createQuery(Device.class);
            Root<Device> deviceRoot = criteria.from(Device.class);
            criteria.select(deviceRoot).where(builder.equal(deviceRoot.get("uuid"), uuid));
            Device device = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            return device;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取设备失败！");
        }
    }

    public static boolean isNewDevice(String uuid) throws DbException {
        return getDevice(uuid) == null;
    }

    public static void addLogAfterLoginFailed(String username) throws DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            Log log = new Log();
            log.setUser(getUser(username));
            log.setTime(new Date());
            session.persist(log);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("插入登录记录失败！");
        }
    }

    public static boolean login(String username, String password) throws RequestException, DbException {
        User user = getUser(username);
        if (user == null) {
            throw new RequestException("用户名不存在！");
        }
        if (user.getLogs().size() >= 10) {
            throw new RequestException("密码输入错误次数过多，请稍后再试！");
        }
        if (!user.getPassword().equals(password)) {
            addLogAfterLoginFailed(username);
            throw new RequestException("密码错误！");
        }
        return true;
    }

    public static String getActivateCode(String username) throws RequestException, DbException {
        User user = getUser(username);
        if (user == null) {
            throw new RequestException("用户名不存在！");
        }
        if (user.getActivateCode() == null) {
            throw new DbException("验证码没有生成！");
        }
        return user.getActivateCode();
    }

    public static String getResetCode(String username) throws RequestException, DbException {
        User user = getUser(username);
        if (user == null) {
            throw new RequestException("用户名不存在！");
        }
        if (user.getResetCode() == null) {
            throw new DbException("验证码没有生成！");
        }
        return user.getResetCode();
    }

    public static String getAvatarPath(String username) throws RequestException, DbException {
        User user = getUser(username);
        if (user == null) {
            throw new RequestException("用户名不存在！");
        }
        if (user.getAvatar() == null) {
            throw new RequestException("用户头像不存在！");
        }
        return user.getAvatar();
    }

    public static boolean setAvatarPath(String username, String avatarPath) throws RequestException, DbException {
        User user = getUser(username);
        if (user == null) {
            throw new RequestException("用户名不存在！");
        }
        user.setAvatar(avatarPath);
        updateUser(factory.openSession(), user);
        return true;
    }

    public static List<Device> getDevices(String username) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Device> criteria = builder.createQuery(Device.class);
            Root<Device> deviceRoot = criteria.from(Device.class);
            criteria.select(deviceRoot).where(builder.equal(deviceRoot.get("username"), username));
            List<Device> devices = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            if (devices == null || devices.size() == 0) {
                throw new RequestException("没有找到设备！");
            }
            return devices;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取设备失败！");
        }
    }

    public static List<String> getUuids(String username) throws RequestException, DbException {
        List<String> uuids = new ArrayList<>();
        for (Device device : getDevices(username)) {
            uuids.add(device.getUuid());
        }
        return uuids;
    }

    public static List<String> findOtherUuids(String uuid) throws RequestException, DbException {
        Session session = null;
        try {
            session = factory.openSession();
            session.getTransaction().begin();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Device> criteria = builder.createQuery(Device.class);
            Root<Device> deviceRoot = criteria.from(Device.class);
            criteria.select(deviceRoot).where(builder.equal(deviceRoot.get("uuid"), uuid));
            Device device = session.createQuery(criteria).uniqueResult();
            session.getTransaction().commit();
            if (device == null) {
                throw new RequestException("没有找到设备！");
            }
            List<String> uuids = getUuids(device.getUser().getUsername());
            uuids.remove(uuid);
            return uuids;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new DbException("获取设备失败！");
        }
    }
}
