package com.example.utils;

import org.hibernate.Session;

public class SessionUtil {

	public static Session openSession() {
		return HibernateUtil.getSessionfactory().openSession();
	}

	public static void closeSession(Session session) {
		if (session.isOpen()) {
			session.close();
		}
	}
}