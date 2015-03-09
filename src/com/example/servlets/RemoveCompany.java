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

@WebServlet("/removecompany")
public class RemoveCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("companyRemoveId"));

		Session dataBaseSession = SessionUtil.openSession();
		dataBaseSession.beginTransaction();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
		Company comapnySelectedForRemoval = companyDao.findByUniqueParameter(Constants.SEARCH_BY_ID, id);
		if (comapnySelectedForRemoval != null) {
			companyDao.delete(comapnySelectedForRemoval);
		}
		dataBaseSession.getTransaction().commit();
		//TODO remove close session
		SessionUtil.closeSession(dataBaseSession);
		
		request.getSession().setAttribute("userNotification", Constants.SUCCESSFUL_REMOVE);
		response.sendRedirect("companies");
	}

}