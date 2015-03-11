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
		try {
			long id = Long.parseLong(request.getParameter("viewId"));

			Session dataBaseSession = SessionUtil.getINSTANCE();
			SessionUtil.beginTransaction();
			GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
			Company comapnySelectedForView = companyDao.findByUniqueParameter(Constants.SEARCH_BY_ID, id);
			SessionUtil.commitTransaction();

			request.setAttribute("comapnySelectedForView", comapnySelectedForView);
			request.getRequestDispatcher("jsp/viewCompany.jsp").forward(request, response);
		} catch (Exception e) {
			response.sendRedirect("error");
		}
	}

}