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
import com.example.entities.Department;
import com.example.entities.Employee;
import com.example.entities.Position;
import com.example.utils.ApplicationUtil;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepartmentTest {

	static GenericDaoImpl<Department> departmentDao;
	static GenericDaoImpl<Company> companyDao;
	static Department department;
	static Department department1;
	static Department department2;
	static Company company;
	static Session session;

	// Unique values needed to comply with unique constraints
	static final String departmentName = ApplicationUtil.uuidString();
	static final String companyName = ApplicationUtil.uuidString();
	static long departmentId;
	static long departmentId1;

	@BeforeClass
	public static void initializeSession() {
		session = SessionUtil.getINSTANCE();
		departmentDao = new GenericDaoImpl<Department>(session, Department.class);
		companyDao = new GenericDaoImpl<Company>(session, Company.class);
	}

	@Before
	public void beginTransaction() {
		// check if session was closed while testing for exceptions
		if (!session.isOpen()) {
			session = SessionUtil.getINSTANCE();
			departmentDao.setCurrentSession(session);
			companyDao.setCurrentSession(session);
		}
		SessionUtil.beginTransaction();
	}

	@Test
	public void aDepartmentShouldBeCreatedByCompany() {
		company = new Company(companyName, "Piccadilly Circus", new Date(), ApplicationUtil.uuidString(), "url/to/company/logo");
		department = new Department(departmentName, "We hire talents", "url/to/new/logo", company);

		company.addDepartment(department);
		companyDao.createOrUpdate(company);

		departmentId = department.getId();

		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		assertTrue("Department is not saved to db", department.equals(departmentFromDb));
		assertTrue("Department is not part of company list of departments", company.getDepartments().contains(departmentFromDb));
		assertTrue("Department company is not properly set", departmentFromDb.getCompany().equals(company));
	}

	@Test
	public void bDepartmentShouldBeCreated() {
		Department localDepartment = new Department(ApplicationUtil.uuidString(), "Quality Assurance department", "url/to/logo", company);

		company.addDepartment(localDepartment);
		departmentDao.createOrUpdate(localDepartment);

		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, localDepartment.getId());
		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);

		assertTrue("Department is not saved to db", localDepartment.equals(departmentFromDb));
		assertTrue("Department is not part of company list of department", companyFromDb.getDepartments().contains(departmentFromDb));
		assertTrue("Department company is not properly set", departmentFromDb.getCompany().equals(companyFromDb));
	}

	@Test
	public void cDepartmentShouldBeUpdated() {
		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);

		String departmentDescription = ApplicationUtil.uuidString();

		departmentFromDb.setDescription(departmentDescription);
		departmentDao.createOrUpdate(departmentFromDb);

		Department departmentUpdated = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		assertTrue("Test if department is updated", departmentFromDb.getDescription().equals(departmentUpdated.getDescription()));
	}

	@Test
	public void dSameDepartmentShouldNotBeAddedTwice() {
		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);

		int before = company.getDepartments().size();
		company.addDepartment(departmentFromDb);
		int after = company.getDepartments().size();

		assertTrue("Test if same object can be added more than once", before == after);
	}

	@Test
	public void eMultipleDepatmentsShouldBeAddedAtOnce() {
		department1 = new Department(ApplicationUtil.uuidString(), "Department Description", "url/to/logo", company);
		department2 = new Department(ApplicationUtil.uuidString(), "Department Description", "url/to/new/logo", company);

		company.addDepartment(department1);
		company.addDepartment(department2);

		companyDao.createOrUpdate(company);

		SessionUtil.commitTransaction();
		SessionUtil.beginTransaction();

		departmentId1 = department1.getId();
		long departmentId2 = department2.getId();

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);
		Department departmentFromDb1 = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId1);
		Department departmentFromDb2 = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId2);

		assertTrue("Department is not saved to db", departmentFromDb1.equals(department1));
		assertTrue("Department is not saved to db", departmentFromDb2.equals(department2));
		assertTrue("Department is not part of company list of department", companyFromDb.getDepartments().contains(departmentFromDb1));
		assertTrue("Department is not part of company list of department", companyFromDb.getDepartments().contains(departmentFromDb2));
		assertTrue("Department company is not properly set", departmentFromDb1.getCompany().equals(companyFromDb));
		assertTrue("Department company is not properly set", departmentFromDb2.getCompany().equals(companyFromDb));
	}

	public void fAllDepartmentsShouldBeFound() {
		List<Department> allDepartments = departmentDao.findAll("Department");

		assertTrue("This department should be part of the list of all departments", allDepartments.contains(department));
		assertTrue("This department should be part of the list of all departments", allDepartments.contains(department1));
		assertTrue("This department should be part of the list of all departments", allDepartments.contains(department2));
		assertTrue("List size should be greater or equal to the number of departments recently added", allDepartments.size() >= 3);
	}

	@Test
	public void gListOfDepartmentsShouldBeFoundByNonUniqueParameter() {
		List<Department> resultList = departmentDao.findByNonUniqueParameter(Constants.SEARCH_BY_DESCRIPTION, "Department Description");

		assertFalse("This department should not be part of the list", resultList.contains(department));
		assertTrue("This department should be part of the list", resultList.contains(department1));
		assertTrue("This department should be part of the list", resultList.contains(department2));
		assertTrue("Result list size should be greater or equal to the number of companies recently added", resultList.size() >= 2);
	}

	@Test
	public void hListShouldBeFoundByMultipleParameters() {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put(Constants.SEARCH_BY_DESCRIPTION, "Department Description");
		parametersMap.put(Constants.SEARCH_BY_LOGO, "url/to/new/logo");

		List<Department> resultList = departmentDao.findBySetOfParameters(parametersMap);
		assertFalse("This department should be part of the list", resultList.contains(department));
		assertFalse("This department should be part of the list", resultList.contains(department1));
		assertTrue("This department should be part of the list", resultList.contains(department2));
		assertTrue("Result list size should be greater or equal to the number of companies recently added", resultList.size() >= 1);
	}

	@Test
	public void iRecordsShouldBeFoundByMultipleIds() {
		List<Long> idList = new ArrayList<Long>();
		idList.add(department.getId());
		idList.add(department1.getId());
		idList.add(department2.getId());

		List<Department> departments = departmentDao.findMultipleRecordsById(idList);
		assertTrue("Department is not part of the list", departments.contains(department));
		assertTrue("Department is not part of the list", departments.contains(department1));
		assertTrue("Department is not part of the list", departments.contains(department2));
	}

	@Test
	public void jDeletingDeparmentShouldNotRemoveItsEmployees() {
		GenericDaoImpl<Employee> employeeDao = new GenericDaoImpl<Employee>(session, Employee.class);

		Department devDepartment = new Department("Development " + ApplicationUtil.uuidString(), "Description", "url/to//logo", company);
		Department qualityAssuranceDepartment = new Department("Quality Assurance " + ApplicationUtil.uuidString(), "Description", "url/to//logo", company);
		Department managementDepartment = new Department("Management " + ApplicationUtil.uuidString(), "Description", "url/to//logo", company);

		Position position = new Position("Undefined Position", (short) 1);

		String JSEemail = ApplicationUtil.uuidString();
		String SEemail = ApplicationUtil.uuidString();
		String SSEemail = ApplicationUtil.uuidString();

		String JQAemail = ApplicationUtil.uuidString();
		String QAemail = ApplicationUtil.uuidString();
		String SQAemail = ApplicationUtil.uuidString();

		String TLemail = ApplicationUtil.uuidString();
		String PMemail = ApplicationUtil.uuidString();

		Employee JSE = new Employee("JSE", (short) 34, "King Street, London", ApplicationUtil.uuidString(), JSEemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);
		Employee SE = new Employee("SE", (short) 34, "King Street, London", ApplicationUtil.uuidString(), SEemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);
		Employee SSE = new Employee("SSE", (short) 34, "King Street, London", ApplicationUtil.uuidString(), SSEemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);

		Employee JQA = new Employee("JQA", (short) 34, "King Street, London", ApplicationUtil.uuidString(), JQAemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);
		Employee QA = new Employee("QA", (short) 34, "King Street, London", ApplicationUtil.uuidString(), QAemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);
		Employee SQA = new Employee("SQA", (short) 34, "King Street, London", ApplicationUtil.uuidString(), SQAemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);

		Employee TL = new Employee("TL", (short) 34, "King Street, London", ApplicationUtil.uuidString(), TLemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);
		Employee PM = new Employee("PM", (short) 34, "King Street, London", ApplicationUtil.uuidString(), PMemail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);

		departmentDao.createOrUpdate(devDepartment);
		departmentDao.createOrUpdate(qualityAssuranceDepartment);
		departmentDao.createOrUpdate(managementDepartment);

		employeeDao.createOrUpdate(JSE);
		employeeDao.createOrUpdate(SE);
		employeeDao.createOrUpdate(SSE);

		employeeDao.createOrUpdate(JQA);
		employeeDao.createOrUpdate(QA);
		employeeDao.createOrUpdate(SQA);

		employeeDao.createOrUpdate(TL);
		employeeDao.createOrUpdate(PM);

		devDepartment.addEmployee(JSE);
		devDepartment.addEmployee(SE);
		devDepartment.addEmployee(SSE);

		qualityAssuranceDepartment.addEmployee(JQA);
		qualityAssuranceDepartment.addEmployee(QA);
		qualityAssuranceDepartment.addEmployee(SQA);

		managementDepartment.addEmployee(TL);
		managementDepartment.addEmployee(PM);

		company.addDepartment(devDepartment);
		company.addDepartment(qualityAssuranceDepartment);
		company.addDepartment(managementDepartment);

		departmentDao.createOrUpdate(devDepartment);
		departmentDao.createOrUpdate(qualityAssuranceDepartment);
		departmentDao.createOrUpdate(managementDepartment);

		long devDepartmentId = devDepartment.getId();
		long qualityAssuranceDepartmentId = qualityAssuranceDepartment.getId();

		List<Long> idList = new ArrayList<Long>();
		idList.add(qualityAssuranceDepartmentId);
		idList.add(devDepartmentId);

		List<Department> departments = departmentDao.findMultipleRecordsById(idList);

		assertTrue("Department is not part of the list", departments.contains(qualityAssuranceDepartment));
		assertTrue("Department is not part of the list", departments.contains(devDepartment));
		assertFalse("Department is part of the list", departments.contains(managementDepartment));

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);

		companyFromDb.removeDepartment(qualityAssuranceDepartment);
		companyFromDb.removeDepartment(devDepartment);

		JSE.removeDepartment(devDepartment);
		SE.removeDepartment(devDepartment);
		SSE.removeDepartment(devDepartment);

		JQA.removeDepartment(qualityAssuranceDepartment);
		QA.removeDepartment(qualityAssuranceDepartment);
		SQA.removeDepartment(qualityAssuranceDepartment);

		departmentDao.deleteMultipleRecords("Department", idList);

		companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);

		assertFalse(companyFromDb.getDepartments().contains(devDepartment));
		assertFalse(companyFromDb.getDepartments().contains(qualityAssuranceDepartment));
		assertTrue(companyFromDb.getDepartments().contains(managementDepartment));

		assertTrue(companyFromDb.getEmployees().contains(JSE));
		assertTrue(companyFromDb.getEmployees().contains(SE));
		assertTrue(companyFromDb.getEmployees().contains(SSE));

		assertTrue(companyFromDb.getEmployees().contains(JQA));
		assertTrue(companyFromDb.getEmployees().contains(QA));
		assertTrue(companyFromDb.getEmployees().contains(SQA));

		assertTrue(companyFromDb.getEmployees().contains(TL));
		assertTrue(companyFromDb.getEmployees().contains(PM));
	}

	@Test
	public void kEmployeesSuitableForBossShouldBeFiltered() {
		GenericDaoImpl<Employee> employeeDao = new GenericDaoImpl<Employee>(session, Employee.class);

		Position jsePosition = new Position("JSE", (short) 3);
		Position tlPosition = new Position("TL", (short) 2);
		Position pmPosition = new Position("PM", (short) 2);
		Position ceoPosition = new Position("CEO", (short) 1);
		Position ctoPosition = new Position("CTO", (short) 1);

		Employee JSE = new Employee("JSE", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image",
				company, jsePosition);

		Employee TL = new Employee("TL", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company,
				tlPosition);
		Employee PM = new Employee("PM", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image", company,
				pmPosition);

		Employee CEO = new Employee("CEO", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image",
				company, ceoPosition);
		Employee CTO = new Employee("CTO", (short) 34, "King Street, London", ApplicationUtil.uuidString(), ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image",
				company, ctoPosition);

		employeeDao.createOrUpdate(JSE);

		employeeDao.createOrUpdate(TL);
		employeeDao.createOrUpdate(PM);

		employeeDao.createOrUpdate(CEO);
		employeeDao.createOrUpdate(CTO);

		department.addEmployee(JSE);
		department.addEmployee(TL);
		department.addEmployee(PM);
		department.addEmployee(CEO);
		department.addEmployee(CTO);

		List<Employee> suitableEmployees = department.getEmployeesSuitableForBoss();

		assertFalse("Company boss list does not contain employee", suitableEmployees.contains(CEO));
		assertFalse("Company boss list does not contain employee", suitableEmployees.contains(CTO));
		assertTrue("Company boss list does contain employee", suitableEmployees.contains(PM));
		assertTrue("Company boss list does contain employee", suitableEmployees.contains(TL));
		assertFalse("Company boss list does contain employee", suitableEmployees.contains(JSE));
	}

	/*
	 * Following test should throw exception, because Department object with non-valid parameters or without any is trying to be saved
	 */

	@Test
	public void lDepartmentsWithSameNameAndCompanyShouldNotBeSaved() {
		Department localDepartment = new Department(departmentName, "Moto", "url/to/new/logo", company);

		try {
			departmentDao.createOrUpdate(localDepartment);
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("department_name_key", e.getConstraintName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void mEmptyDepartmentShouldNotBeSaved() {
		Department localDepartment = new Department();
		try {
			departmentDao.createOrUpdate(localDepartment);
			fail("PropertyValueException should have been thrown");
		} catch (PropertyValueException e) {
			assertEquals("name", e.getPropertyName());
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void nDepartmentNameLengthConstraintTest() {
		String longName = ApplicationUtil.longUuidString().concat(ApplicationUtil.longUuidString());

		Department localDepartment = new Department(longName, "Moto", "url/to/new/logo", company);
		try {
			departmentDao.createOrUpdate(localDepartment);
			fail("DataException should have been thrown");
		} catch (DataException e) {
			assertTrue(e.getSQLException().getMessage().contains("value too long"));
			SessionUtil.rollbackTransaction();
			SessionUtil.closeSession();
		}
	}

	@Test
	public void xDepartmentShouldBeDeleted() {
		company.removeDepartment(department1);
		departmentDao.delete(department1);

		Department deletedDepartment = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId1);
		assertNull("Check if department was deleted by its DAO", deletedDepartment);

	}

	@Test
	public void yDepartmentShouldBeDeletedByParentCompany() {
		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		company = companyDao.findByUniqueParameter(Constants.SEARCH_BY_NAME, companyName);

		if (company.getDepartments().contains(departmentFromDb)) {
			company.removeDepartment(departmentFromDb);
		} else {
			fail("Assert that company has that department");
		}
		companyDao.createOrUpdate(company);

		Department deletedDepartment = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		assertNull("Check if department was deleted by its parent comapny", deletedDepartment);
		assertFalse("Department is part of company list of departments", company.getDepartments().contains(deletedDepartment));
	}

	@Test
	public void zCompanySholdClearAllAssociatedEntities() {
		companyDao.delete(company);

		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		assertNull("Check if all departments mapped to company were deleted by cascade", departmentFromDb);

		List<Long> idList = new ArrayList<Long>();
		idList.add(department.getId());
		idList.add(department1.getId());
		idList.add(department2.getId());

		List<Department> departments = departmentDao.findMultipleRecordsById(idList);

		assertFalse("Department is not successfully deleted", departments.contains(department));
		assertFalse("Department is not successfully deleted", departments.contains(department1));
		assertFalse("Department is not successfully deleted", departments.contains(department2));
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