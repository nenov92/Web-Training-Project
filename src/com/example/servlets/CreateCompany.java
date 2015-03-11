package com.example.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Company company = null;

		// to inform user whether company was successfully created/updated or not
		String userNotification = null;

		// Data received from jsp
		String name = null;
		String address = null;
		Date establishedDate = null;
		String bulstat = null;
		Part logo = null;
		Long bossId = null;

		// name and path for logo
		String fileName = null;
		String filePath = null;

		// Data received from jsp validation
		if (request.getParameter("companyName") != null && (request.getParameter("companyName").length() < 50)) {
			name = request.getParameter("companyName");
		}
		if (request.getParameter("companyAddress") != null && (request.getParameter("companyAddress").length() < 100)) {
			address = request.getParameter("companyAddress");
		}
		if (request.getParameter("companyEstablishedDate") != null) {
			establishedDate = ApplicationUtil.formatDate(request.getParameter("companyEstablishedDate"));
		}
		if (request.getParameter("companyBulstat") != null && (request.getParameter("companyBulstat").length() < 50)) {
			bulstat = request.getParameter("companyBulstat");
			filePath = Constants.FILE_PATH(bulstat);
		}
		if (request.getPart("companyLogo") != null) {
			logo = request.getPart("companyLogo");
		}
		if (request.getParameter("companyBoss") != null) {
			bossId = Long.parseLong(request.getParameter("companyBoss"));
		}

		if (filePath != null && ApplicationUtil.saveImage(logo, filePath)) {
			fileName = Constants.FILE_NAME(bulstat);
		}

		try {
			Session dbSession = SessionUtil.getINSTANCE();
			GenericDaoImpl<Company> companyDao = new GenericDaoImpl<Company>(dbSession, Company.class);
			GenericDaoImpl<Employee> employeeDao;
			SessionUtil.beginTransaction();
			company = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, bulstat);

			if (name != null && address != null && establishedDate != null && bulstat != null) {
				// if company does not exist in db it will be created, otherwise it will be updated
				if (company == null) {
					if (fileName != null) {
						company = new Company(name, address, establishedDate, bulstat, fileName);
						userNotification = Constants.SUCCESSFUL_CREATE;
					}
				} else {
					company.setAddress(address);
					company.setEstablishedDate(establishedDate);
					if (fileName != null) {
						company.setLogo(fileName);
					}
					if (bossId != null) {
						employeeDao = new GenericDaoImpl<Employee>(dbSession, Employee.class);
						Employee boss = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_ID, bossId);
						company.setBoss(boss);
					}
					userNotification = Constants.SUCCESSFUL_EDIT;
				}
			}
			if (company != null) {
				companyDao.createOrUpdate(company);
			} else {
				userNotification = Constants.UNSUCCESSFUL_OUTCOME;
			}
			SessionUtil.commitTransaction();

			request.getSession().setAttribute("userNotification", userNotification);
			response.sendRedirect("companies");
		} catch (Exception e) {
			response.sendRedirect("error");
		}
	}

}