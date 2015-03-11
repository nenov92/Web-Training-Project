package com.example.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@WebServlet("/validateform")
public class ValidateFormData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String companyName = request.getParameter("companyName");
		String companyBulstat = request.getParameter("companyBulstat");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (companyName != null) {
			if (isPropertyAlreadyTaken(Constants.SEARCH_BY_NAME, companyName)) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "name is already taken");
			} else {
				response.getWriter().write("{\"success\": \"okay\"}");
			}
		}

		if (companyBulstat != null) {
			if (isPropertyAlreadyTaken(Constants.SEARCH_BY_BULSTAT, companyBulstat)) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "bulstat is already taken");
			} else {
				response.getWriter().write("{\"success\": \"okay\"}");
			}
		}
	}

	private boolean isPropertyAlreadyTaken(String searchBy, String parameterValue) {
		try {
			Session dbSession = SessionUtil.getINSTANCE();
			GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dbSession, Company.class);
			SessionUtil.beginTransaction();
			Company company = companyDao.findByUniqueParameter(searchBy, parameterValue);
			SessionUtil.commitTransaction();

			if (company == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
	}

}