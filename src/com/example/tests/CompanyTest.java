package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.example.daos.GenericDaoImpl;
import com.example.entities.Company;
import com.example.entities.Employee;
import com.example.entities.Position;
import com.example.utils.ApplicationUtil;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompanyTest {

	static GenericDaoImpl<Company> companyDao;
	static Company company;
	static Company company1;
	static Company company2;
	static Company companyOne;
	static Company companyTwo;
	static Session session;

	// Unique values needed to comply with unique constraints
	static final String companyName = ApplicationUtil.uuidString();
	static final String companyName1 = ApplicationUtil.uuidString();
	static final String companyName2 = ApplicationUtil.uuidString();
	static final String companyNameOne = ApplicationUtil.uuidString();
	static final String companyNameTwo = ApplicationUtil.uuidString();
	static final String companyBulstat = ApplicationUtil.uuidString();

	@BeforeClass
	public static void initializeSession() {
		session = SessionUtil.getINSTANCE();
		companyDao = new GenericDaoImpl<Company>(session, Company.class);
	}

	@Before
	public void beginTransaction() {
		// check if session was closed while testing for exceptions
		if (!session.isOpen()) {
			session = SessionUtil.getINSTANCE();
			companyDao.setCurrentSession(session);
		}
		SessionUtil.beginTransaction();
	}

	@Test
	public void aCompanyShouldBeCreated() {
		company = new Company(companyName, "George Street", new Date(), companyBulstat, "url/to/company/logo");
		companyDao.createOrUpdate(company);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);
		assertTrue("Company is not saved to db", company.equals(companyFromDb));
	}

	@Test
	public void bCompanyShouldBeUpdate() {
		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);

		companyFromDb.setAddress("Oxford Street");
		companyDao.createOrUpdate(companyFromDb);

		Company updatedCompany = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);
		assertTrue("Company name is not updated in db", companyFromDb.getAddress().equals(updatedCompany.getAddress()));
	}

	@Test
	public void cAllCompaniesShouldBeFound() {
		company1 = new Company(companyName1, "Oxford Street", new Date(), ApplicationUtil.uuidString(), "url/to/company/logo");
		company2 = new Company(companyName2, "Oxford Street", new Date(), ApplicationUtil.uuidString(), "url/to/company/new/logo");
		companyDao.createOrUpdate(company1);
		companyDao.createOrUpdate(company2);

		List<Company> allCompanies = companyDao.findAll("Company");

		assertTrue("Company is not part of the list", allCompanies.contains(company));
		assertTrue("Company is not part of the list", allCompanies.contains(company1));
		assertTrue("Company is not part of the list", allCompanies.contains(company2));
		assertTrue("List size is not greater or equal to the number of companies recently added", allCompanies.size() >= 3);
	}

	@Test
	public void dListOfCompaniesShouldBeFound() {
		List<Company> resultList = companyDao.findByNonUniqueParameter("address", "Oxford Street");

		assertTrue("Company is not part of the list", resultList.contains(company));
		assertTrue("Company is not part of the list", resultList.contains(company1));
		assertTrue("Company is not part of the list", resultList.contains(company2));
		assertTrue("Result list size is not greater or equal to the number of companies recently added", resultList.size() >= 3);
	}

	@Test
	public void eListShouldBeFoundByMultipleParameters() {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put(Constants.SEARCH_BY_ADDRESS, "Oxford Street");
		parametersMap.put(Constants.SEARCH_BY_LOGO, new Date());

		List<Company> resultList = companyDao.findBySetOfParameters(parametersMap);
		assertTrue("Company is not part of the list", resultList.contains(company));
		assertTrue("Company is not part of the list", resultList.contains(company1));
		assertFalse("Company is not part of the list", resultList.contains(company2));
		assertTrue("Result list size is not greater or equal to the number of companies recently added", resultList.size() >= 2);
	}

	@Test
	public void fRecordsShouldBeFoundByMultipleIds() {
		List<Long> idList = new ArrayList<Long>();
		idList.add(company.getId());
		idList.add(company1.getId());
		idList.add(company2.getId());

		List<Company> companies = companyDao.findMultipleRecordsById(idList);
		assertTrue("Company is not part of the list", companies.contains(company));
		assertTrue("Company is not part of the list", companies.contains(company1));
		assertTrue("Company is not part of the list", companies.contains(company2));
	}

	@Test
	public void gEmployeesSuitableForBossShouldBeFiltered() {
		Position jsePosition = new Position("JSE", (short) 3);
		Position tlPosition = new Position("TL", (short) 2);
		Position pmPosition = new Position("PM", (short) 2);
		Position ceoPosition = new Position("CEO", (short) 1);
		Position ctoPosition = new Position("CTO", (short) 1);

		Employee JSE = new Employee("JSE", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company, jsePosition);
		
		Employee TL = new Employee("TL", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company, tlPosition);
		Employee PM = new Employee("PM", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company, pmPosition);

		Employee CEO = new Employee("CEO", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company, ceoPosition);
		Employee CTO = new Employee("CTO", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company, ctoPosition);

		company.addEmployee(JSE);
		company.addEmployee(TL);
		company.addEmployee(PM);
		company.addEmployee(CEO);
		company.addEmployee(CTO);
		
		List<Employee> suitableEmployees = company.getEmployeesSuitableForBoss();
		
		assertTrue("Company boss list does not contain employee", suitableEmployees.contains(CEO));
		assertTrue("Company boss list does not contain employee", suitableEmployees.contains(CTO));
		assertFalse("Company boss list does contain employee", suitableEmployees.contains(PM));
		assertFalse("Company boss list does contain employee", suitableEmployees.contains(TL));
		assertFalse("Company boss list does contain employee", suitableEmployees.contains(JSE));
	}

	@Test
	public void hCompanyRatingsShouldBeSwapped() {
		companyOne = new Company(companyNameOne, "University Street", new Date(), ApplicationUtil.uuidString(), "url");
		companyTwo = new Company(companyNameTwo, "University Street", new Date(), ApplicationUtil.uuidString(), "url");
		
		companyDao.createOrUpdate(companyOne);
		companyDao.createOrUpdate(companyTwo);
		
		companyOne = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameOne);
		companyTwo = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameTwo);
		
		long ratingOne = companyOne.getRating();
		long ratingTwo = companyTwo.getRating();

		companyTwo.setRating(-1);
		companyDao.createOrUpdate(companyTwo);
		SessionUtil.commitTransaction();

		SessionUtil.beginTransaction();
		companyOne.setRating(ratingTwo);
		companyDao.createOrUpdate(companyOne);

		companyTwo.setRating(ratingOne);
		companyDao.createOrUpdate(companyTwo);
		SessionUtil.commitTransaction();
		
		SessionUtil.beginTransaction();
		companyOne = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameOne);
		companyTwo = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameTwo);
		assertEquals(ratingTwo, companyOne.getRating());
		assertEquals(ratingOne, companyTwo.getRating());
	}
	
	/*
	 * Following tests should throw exception, because Company object with non-valid parameters or without any is trying to be saved
	 */

	@Test
	public void iWhenErrorOccursWhileSwappingRollbackShouldBeExecuted() {
		Company localCompanyOne = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameOne);
		Company localCompanyTwo = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameTwo);

		long ratingOne = localCompanyOne.getRating();
		long ratingTwo = localCompanyTwo.getRating();

		try {
			localCompanyOne.setRating(ratingTwo);
			localCompanyTwo.setRating(ratingOne);
			
			companyDao.createOrUpdate(localCompanyOne);
			companyDao.createOrUpdate(localCompanyTwo);
			SessionUtil.commitTransaction();
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("company_rating_key", e.getConstraintName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
		
		// reinitialize session after it was closed
		session = SessionUtil.getINSTANCE();
		companyDao.setCurrentSession(session);
		
		localCompanyOne = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameOne);
		localCompanyTwo = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyNameTwo);
		
		assertEquals(ratingOne, localCompanyOne.getRating());
		assertEquals(ratingTwo, localCompanyTwo.getRating());
	}
	
	@Test
	public void mMultipleCompaniesWithSameNameShouldNotBeSaved() {
		Company localCompany = new Company(companyName1, "Bedford Street", new Date(), ApplicationUtil.uuidString(), "url/to/company/new/logo");

		try {
			companyDao.createOrUpdate(localCompany);
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("company_name_key", e.getConstraintName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void nMultipleCompaniesWithSameBulstatShouldNotBeSaved() {
		Company localCompany = new Company(ApplicationUtil.uuidString(), "George Street", new Date(), companyBulstat, "url/to/company/new/logo");

		try {
			companyDao.createOrUpdate(localCompany);
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("company_bulstat_key", e.getConstraintName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void pEmptyCompanyShouldNotBeSaved() {
		Company localCompany = new Company();
		try {
			companyDao.createOrUpdate(localCompany);
			fail("PropertyValueException should have been thrown");
		} catch (PropertyValueException e) {
			assertEquals("address", e.getPropertyName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void qCompanyNameLengthConstraintTest() {
		String longName = ApplicationUtil.longUuidString().concat(ApplicationUtil.longUuidString());

		Company localCompany = new Company(longName, "Bedford Street", new Date(), ApplicationUtil.uuidString(), "url/to/company/new/logo");
		try {
			companyDao.createOrUpdate(localCompany);
			fail("DataException should have been thrown");
		} catch (DataException e) {
			assertTrue(e.getSQLException().getMessage().contains("value too long"));
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void xSingleCompanyShouldBeDeleted() {
		companyDao.delete(company);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);
		assertNull("Company is not successfully deleted", companyFromDb);
	}

	@Test
	public void zMultipleCompaniesShouldBeDeleted() {
		List<Long> idList = new ArrayList<Long>();
		idList.add(company1.getId());
		idList.add(company2.getId());
		idList.add(companyOne.getId());
		idList.add(companyTwo.getId());

		companyDao.deleteMultipleRecords("Company", idList);
		List<Company> companies = companyDao.findMultipleRecordsById(idList);

		assertFalse("Company is not successfully deleted", companies.contains(company1));
		assertFalse("Company is not successfully deleted", companies.contains(company2));
		assertFalse("Company is not successfully deleted", companies.contains(companyOne));
		assertFalse("Company is not successfully deleted", companies.contains(companyTwo));
		assertTrue("List of companies is not empty", companies.isEmpty());
	}
	
	@After
	public void commitTransaction() {
		SessionUtil.commitTransaction();
	}

	@AfterClass
	public static void closeRunningSession() {
		SessionUtil.closeSession();
	}

}