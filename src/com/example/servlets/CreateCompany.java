package com.example.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		String name = request.getParameter("companyName");
		String address = request.getParameter("companyAddress");
		Date establishedDate = ApplicationUtil.formatDate(request.getParameter("companyEstablishedDate"));
		String bulstat = request.getParameter("companyBulstat");
		Part logo = request.getPart("companyLogo");
		String bossIdAsString = request.getParameter("companyBoss");
		Long bossId = null;

		if (bossIdAsString != null) {
			bossId = Long.parseLong(bossIdAsString);
		}

		InputStream logoContent = null;
		OutputStream logoOut = null;

		String fileName = "images/img" + bulstat + ".jpg";
		String filePath = "C:/DevTools/Apache Tomcat v7.0/wtpwebapps/MyWebProjectStaticContent" + File.separator + fileName;

		if (logo.getContentType().startsWith("image")) {
			try {
				logoContent = logo.getInputStream();

				logoOut = new FileOutputStream(new File(filePath));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = logoContent.read(bytes)) != -1) {
					logoOut.write(bytes, 0, read);
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				if (logoContent != null) {
					try {
						logoContent.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (logoOut != null) {
					try {
						logoOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		Company company;
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.sendRedirect("companies");
	}
	
}