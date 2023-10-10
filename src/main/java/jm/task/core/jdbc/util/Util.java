package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Connection connection;
    private final static String URL = "jdbc:mysql://localhost:3306/newschema_users";
    private final static String USER = "root";
    private final static String PASSWORD = "More_kish090";

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static class HibernateUtil {
        private static SessionFactory sessionFactory;

        public static SessionFactory getSessionFactory() {
            if (sessionFactory == null) {
                try {
                    Properties settings = new Properties();
                    Configuration configuration = new Configuration();
                    settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                    settings.put(Environment.URL, URL);
                    settings.put(Environment.USER, USER);
                    settings.put(Environment.PASS, PASSWORD);
                    settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                    settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                    settings.put(Environment.HBM2DDL_AUTO, "none");

                    configuration.setProperties(settings).addAnnotatedClass(User.class);
                    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return sessionFactory;
        }

        public static void closeSessionFactory() {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

}
