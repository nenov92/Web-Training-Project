package com.example.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.hibernate.Session;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.entities.Employee;
import com.example.utils.ApplicationUtil;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@WebServlet("/create")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 100)
public class CreateCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Company company = null;

		// Data received from jsp
		String name = request.getParameter("companyName");
		String address = request.getParameter("companyAddress");
		Date establishedDate = ApplicationUtil.formatDate(request.getParameter("companyEstablishedDate"));
		String bulstat = request.getParameter("companyBulstat");
		Part logo = request.getPart("companyLogo");
		String bossIdAsString = request.getParameter("companyBoss");
		Long bossId = null;

		// name and path for logo
		String fileName = Constants.FILE_NAME(bulstat);
		String filePath = Constants.FILE_PATH(bulstat);

		ApplicationUtil.saveImage(logo, filePath);

		if (bossIdAsString != null) {
			bossId = Long.parseLong(bossIdAsString);
		}

		// if company does not exist in db it will be created, otherwise it will be updated
		Session dbSession = SessionUtil.openSession();
		GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dbSession, Company.class);
		GenericDaoImpl<Employee> employeeDao;
		dbSession.beginTransaction();
		if (companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, bulstat) == null) {
			company = new Company(name, address, establishedDate, bulstat, fileName);
		} else {
			company = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, bulstat);
			company.setAddress(address);
			company.setEstablishedDate(establishedDate);
			company.setLogo(fileName);
			if (bossId != null) {
				employeeDao = new GenericDaoImpl<Employee>(dbSession, Employee.class);
				Employee boss = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_ID, bossId);
				company.setBoss(boss);
			}
		}
		companyDao.createOrUpdate(company);
		dbSession.getTransaction().commit();
		SessionUtil.closeSession(dbSession);

		// if company does not exist it will be added to grid & redirect to main screen
		HttpSession httpSession = request.getSession();
		List<Company> companiesFromUserInput = (List<Company>) httpSession.getAttribute("companiesFromUserInput");
		int x = (int) httpSession.getAttribute("xValue");
		int y = (int) httpSession.getAttribute("yValue");
		if (company != null && (!companiesFromUserInput.contains(company)) && (companiesFromUserInput.size() < x * y)) {
			companiesFromUserInput.add(companiesFromUserInput.size(), company);
			httpSession.setAttribute("companiesFromUserInput", companiesFromUserInput);
		}
		response.sendRedirect("companies");
	}

}