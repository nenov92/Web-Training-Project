package com.example.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			System.out.println("Hibernate Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate serviceRegistry created");

			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.print("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionfactory() {
		return sessionFactory;
	}
}