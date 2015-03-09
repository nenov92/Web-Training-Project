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

@WebServlet("/swapcompanies")
public class SwapCompanies extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long idOld = Long.parseLong(request.getParameter("idOld"));
		long idNew = Long.parseLong(request.getParameter("idNew"));

		Session dataBaseSession = SessionUtil.openSession();
		dataBaseSession.beginTransaction();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);

		// use one call to db for both
		Company comapnyForSwapping = companyDao.findByUniqueParameter(Constants.SEARCH_BY_ID, idOld);
		Company comapnyToSwap = companyDao.findByUniqueParameter(Constants.SEARCH_BY_ID, idNew);
		// unit test for rating swapping and rollback
		long ratingForSwapping = comapnyForSwapping.getRating();
		long ratingToSwap = comapnyToSwap.getRating();

		comapnyToSwap.setRating(0);
		companyDao.createOrUpdate(comapnyToSwap);

		dataBaseSession.getTransaction().commit();
		dataBaseSession.beginTransaction();

		comapnyForSwapping.setRating(ratingToSwap);
		companyDao.createOrUpdate(comapnyForSwapping);

		comapnyToSwap.setRating(ratingForSwapping);
		companyDao.createOrUpdate(comapnyToSwap);

		dataBaseSession.getTransaction().commit();
		SessionUtil.closeSession(dataBaseSession);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"success\": \"okay\"}");
	}

}