package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ApplicationUtil {

	public static String uuidString() {
		return new String(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
	}

	public static String longUuidString() {
		return new String(UUID.randomUUID().toString());
	}

	public static Date formatDate(String establishedDate) {
		SimpleDateFormat parsed = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = parsed.parse(establishedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}