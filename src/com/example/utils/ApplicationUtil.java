package com.example.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Part;

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

	public static boolean saveImage(Part image, String filePath) {
		InputStream logoContent = null;
		OutputStream logoOut = null;
		
		if (image.getContentType().startsWith("image")) {
			try {
				logoContent = image.getInputStream();
				logoOut = new FileOutputStream(new File(filePath));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = logoContent.read(bytes)) != -1) {
					logoOut.write(bytes, 0, read);
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				if (logoContent != null) {
					try {
						logoContent.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (logoOut != null) {
					try {
						logoOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}