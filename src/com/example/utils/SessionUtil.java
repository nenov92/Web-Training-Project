package com.example.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionUtil {

	private static SessionFactory sessionFactory = HibernateUtil.getSessionfactory();
	private static Session INSTANCE = null;

	private SessionUtil() {
	}
	
	public static void closeSession() {
		if (INSTANCE.isOpen()) {
			INSTANCE.close();
		}
	}

	public static synchronized Session getINSTANCE() {
		if (INSTANCE == null || !INSTANCE.isOpen()) {
			INSTANCE = sessionFactory.openSession();
		}
		return INSTANCE;
	}

	public static void beginTransaction() {
		if (!INSTANCE.getTransaction().isActive()) {
			INSTANCE.beginTransaction();
		}
	}

	public static void commitTransaction() {
		if (INSTANCE.isOpen() && INSTANCE.getTransaction().isActive() && !INSTANCE.getTransaction().wasRolledBack()) {
			INSTANCE.getTransaction().commit();
		}
	}

	public static void rollbackTransaction() {
		INSTANCE.getTransaction().rollback();
	}

}