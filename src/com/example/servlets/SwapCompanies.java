package com.example.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.utils.SessionUtil;

@WebServlet("/swapcompanies")
public class SwapCompanies extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Long> identifiers = new ArrayList<Long>();
		identifiers.add(Long.parseLong(request.getParameter("idOld")));
		identifiers.add(Long.parseLong(request.getParameter("idNew")));

		Session dataBaseSession = SessionUtil.getINSTANCE();
		SessionUtil.beginTransaction();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
		List<Company> companies = companyDao.findMultipleRecordsById(identifiers);

		Company companyOne = companies.get(0);
		Company companyTwo = companies.get(1);

		long ratingOne = companyOne.getRating();
		long ratingTwo = companyTwo.getRating();

		// temporary values to be set to both companies, so that ConstraintViolationException is escaped when swapping ratings
		long tempRatingOne = -1;
		long tempRatingTwo = -2;

		try {
			companyTwo.setRating(tempRatingOne);
			companyDao.createOrUpdate(companyTwo);

			companyOne.setRating(tempRatingTwo);
			companyDao.createOrUpdate(companyOne);

			// refresh objects by obtaining the new copies from db
			companies = companyDao.findMultipleRecordsById(identifiers);
			companyOne = companies.get(0).getRating() == tempRatingTwo ? companies.get(0) : companies.get(1);
			companyTwo = companies.get(1).getRating() == tempRatingOne ? companies.get(1) : companies.get(0);

			companyOne.setRating(ratingTwo);
			companyDao.createOrUpdate(companyOne);

			companyTwo.setRating(ratingOne);
			companyDao.createOrUpdate(companyTwo);
			SessionUtil.commitTransaction();

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"success\": \"okay\"}");
		} catch (ConstraintViolationException e) {
			// if exception has occurred, revert changes
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}

	}
}