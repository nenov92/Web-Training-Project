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

@WebServlet("/viewcompany")
public class ViewCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("viewId"));

		Session dataBaseSession = SessionUtil.openSession();
		dataBaseSession.beginTransaction();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
		Company comapnySelectedForView = companyDao.findByUniqueParameter(Constants.SEARCH_BY_ID, id);
		dataBaseSession.getTransaction().commit();
		SessionUtil.closeSession(dataBaseSession);

		request.setAttribute("comapnySelectedForView", comapnySelectedForView);
		request.getRequestDispatcher("viewCompany.jsp").forward(request, response);
	}

}