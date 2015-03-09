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
import com.example.utils.SessionUtil;

@WebServlet(name = "ShowCompanies", urlPatterns = { "/companies" })
public class ShowCompanies extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loadCompanies(request, response);
	}

	private void loadCompanies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int x;
		int y;
		// TODO use only x, y from session
		if (request.getParameter("x-axis") != null && request.getParameter("y-axis") != null) {
			x = Integer.parseInt(request.getParameter("x-axis"));
			y = Integer.parseInt(request.getParameter("y-axis"));
		} else if (request.getSession().getAttribute("xValue") != null && request.getSession().getAttribute("yValue") != null) {
			x = (int) request.getSession().getAttribute("xValue");
			y = (int) request.getSession().getAttribute("yValue");
		} else {
			x = 3;
			y = 3;
		}

		try {
			Session dataBaseSession = SessionUtil.openSession();
			GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dataBaseSession, Company.class);
			dataBaseSession.beginTransaction();
			List<Company> companiesFromDb = companyDao.findTop(x * y);
			dataBaseSession.getTransaction().commit();
			// TODO do not close session
			SessionUtil.closeSession(dataBaseSession);

			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("companiesFromDb", companiesFromDb);
			httpSession.setAttribute("xValue", x);
			httpSession.setAttribute("yValue", y);
			request.getRequestDispatcher("jsp/showCompanies.jsp").forward(request, response);
		} catch (Exception e) {
			response.sendRedirect("error");
		}
	}

}