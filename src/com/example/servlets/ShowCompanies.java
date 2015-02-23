package com.example.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.utils.SessionUtil;

@WebServlet(name = "ShowCompanies", urlPatterns = { "/companies" })
public class ShowCompanies extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session dataBaseSession = SessionUtil.openSession();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
		dataBaseSession.beginTransaction();
		List<Company> companiesInitial = companyDao.findTop(9);
		dataBaseSession.getTransaction().commit();
		SessionUtil.closeSession(dataBaseSession);

		request.setAttribute("companiesInitial", companiesInitial);
		request.getRequestDispatcher("showCompanies.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int x = Integer.parseInt(request.getParameter("x-axis"));
		int y = Integer.parseInt(request.getParameter("y-axis"));

		Session dataBaseSession = SessionUtil.openSession();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
		dataBaseSession.beginTransaction();
		List<Company> companiesFromUserInput = companyDao.findTop(x * y);
		dataBaseSession.getTransaction().commit();
		SessionUtil.closeSession(dataBaseSession);

		request.setAttribute("companiesFromUserInput", companiesFromUserInput);
		request.setAttribute("yValue", y);
		request.getRequestDispatcher("showCompanies.jsp").forward(request, response);
	}

}