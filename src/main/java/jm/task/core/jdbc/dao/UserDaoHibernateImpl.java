package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.HibernateUtil.getSessionFactory();
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age TINYINT)")
                    .addEntity(User.class)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS user").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        User user = new User(name, lastName, age);
        try {
            Session session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
            session.save(user);
            System.out.println("User с именем - " + name + " добавлен в базу данных");
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
            session.remove(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void cleanUsersTable() {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DELETE FROM newschema_users.user").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
