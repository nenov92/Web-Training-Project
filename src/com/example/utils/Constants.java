package com.example.utils;

import java.io.File;

public class Constants {

	// Search criteria constants
	public static final String SEARCH_BY_ID = "id";
	public static final String SEARCH_BY_NAME = "name";
	public static final String SEARCH_BY_ADDRESS = "address";
	public static final String SEARCH_BY_LOGO = "logo";
	public static final String SEARCH_BY_RATING = "rating";
	public static final String SEARCH_BY_BULSTAT = "bulstat";
	public static final String SEARCH_BY_PERSONAL_ID = "personalId";
	public static final String SEARCH_BY_EMAIL = "email";
	public static final String SEARCH_BY_DESCRIPTION = "description";
	public static final String SEARCH_BY_AGE = "age";

	// Levels used to filter company or department boss
	public static final short COMPANY_BOSS_LEVEL = 1;
	public static final short DEPARTMENT_BOSS_LEVEL = 2;

	// values send to main screen when entity is created/edited successfully or not
	public static final String SUCCESSFUL_CREATE = "create";
	public static final String SUCCESSFUL_EDIT = "edit";
	public static final String SUCCESSFUL_REMOVE = "remove";
	public static final String UNSUCCESSFUL_OUTCOME = "unsuccess";
	
	// name and path for images
	public static String FILE_NAME(String uniqueObjectIdentifier) {
		return "images/img" + uniqueObjectIdentifier + ".jpg";
	}

	public static String FILE_PATH(String uniqueObjectIdentifier) {
		return "F:/Dev/Java Workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/WebApp/css" + File.separator + FILE_NAME(uniqueObjectIdentifier);
	}

}