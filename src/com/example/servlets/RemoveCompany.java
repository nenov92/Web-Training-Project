package com.example.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@WebServlet("/removecompany")
public class RemoveCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
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
		SessionUtil.closeSession(dataBaseSession);

		HttpSession httpSession = request.getSession();
		List<Company> companiesFromUserInput = (List<Company>) httpSession.getAttribute("companiesFromUserInput");
		companiesFromUserInput.remove(comapnySelectedForRemoval);
		httpSession.setAttribute("companiesFromUserInput", companiesFromUserInput);
		response.sendRedirect("companies");
	}

}