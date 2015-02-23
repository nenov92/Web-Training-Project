package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import com.example.entities.DepartmentEmployee;
import com.example.entities.Employee;
import com.example.entities.Position;
import com.example.utils.ApplicationUtil;
import com.example.utils.Constants;
import com.example.utils.SessionUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeTest {

	static GenericDaoImpl<Employee> employeeDao;
	static GenericDaoImpl<Company> companyDao;
	static GenericDaoImpl<Department> departmentDao;
	static Employee employee;
	static Employee employee1;
	static Employee employee2;
	static Employee employee3;
	static Company company;
	static Department department;
	static Position position;
	static Session session;

	// Unique values needed to comply with unique constraints
	static final String employeeEmail = ApplicationUtil.uuidString();
	static final String employeeEmail1 = ApplicationUtil.uuidString();
	static final String employeeEmail2 = ApplicationUtil.uuidString();
	static final String employeeEmail3 = ApplicationUtil.uuidString();
	static final String employeePersonalId = ApplicationUtil.uuidString();
	static final String employeePersonalId1 = ApplicationUtil.uuidString();
	static final String companyBulstat = ApplicationUtil.uuidString();
	static long departmentId;

	@BeforeClass
	public static void initializeSession() {
		session = SessionUtil.openSession();
		employeeDao = new GenericDaoImpl<Employee>(session, Employee.class);
		companyDao = new GenericDaoImpl<Company>(session, Company.class);
		departmentDao = new GenericDaoImpl<Department>(session, Department.class);
	}

	@Before
	public void beginTransaction() {
		if (!session.isOpen()) {
			session = SessionUtil.openSession();
			employeeDao.setCurrentSession(session);
			companyDao.setCurrentSession(session);
			departmentDao.setCurrentSession(session);
		}
		if (!session.getTransaction().isActive()) {
			session.beginTransaction();
		}
	}

	@Test
	public void aEmployeeShouldBeCreatedByParentCompany() {
		position = new Position("JSE", (short) 1);
		company = new Company(ApplicationUtil.uuidString(), "Random Street", new Date(), companyBulstat, "url/to/company/logo");
		employee = new Employee("Sam James", (short) 34, "King Street, London", employeePersonalId, employeeEmail, "+44 321 213323", new Date(), "url/to/employee/image", company, position);

		company.addEmployee(employee);
		companyDao.createOrUpdate(company);

		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_PERSONAL_ID, employeePersonalId);
		assertTrue("Employee is not saved to db by partent company", employee.equals(employeeFromDb));
		assertTrue("Employee is not part of company list of employees", company.getEmployees().contains(employeeFromDb));
		assertTrue("Employee's company is not properly set", employeeFromDb.getCompany().equals(company));
	}

	@Test
	public void bEmployeeShouldBeCreated() {
		employee1 = new Employee("Steven Nelson", (short) 34, "King Street, Manchester", employeePersonalId1, employeeEmail1, "+44 321 213323", new Date(), "url/to/employee/image", company, position);

		company.addEmployee(employee1);
		employeeDao.createOrUpdate(employee1);

		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail1);
		assertTrue("Employee is not saved to db by its DAO", employee1.equals(employeeFromDb));
		assertTrue("Employee is not part of company list of employees", company.getEmployees().contains(employeeFromDb));
		assertTrue("Employee's company is not properly set", employeeFromDb.getCompany().equals(company));
	}

	@Test
	public void cEmployeeShouldBeAddedToExistingDepartment() {
		department = new Department(ApplicationUtil.uuidString(), "Department Description", "url/to/logo", company);
		company.addDepartment(department);
		departmentDao.createOrUpdate(department);
		departmentId = department.getId();

		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);

		employee2 = new Employee("Peter Parker", (short) 19, "King Street, Manchester", ApplicationUtil.uuidString(), employeeEmail2, "+44 321 213323", new Date(), "url/to/employee/image", company,
				position);

		employeeDao.createOrUpdate(employee2);
		departmentFromDb.addEmployee(employee2);
		departmentDao.createOrUpdate(departmentFromDb);

		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail2);

		long rating = 0;
		for (DepartmentEmployee de : employeeFromDb.getDepartmentEmployees()) {
			rating = de.getRating();
		}

		assertTrue("Employee is not saved to db by partent department", employee2.equals(employeeFromDb));
		assertTrue("Employee is not part of department list of employees", departmentFromDb.getEmployees().contains(employeeFromDb));
		assertTrue("Employee is not part of company list of employees", company.getEmployees().contains(employeeFromDb));
		assertTrue("Employee's company is not properly set", employeeFromDb.getCompany().equals(company));
		assertTrue("Employee's department is not properly set", employeeFromDb.getDepartments().contains(departmentFromDb));
		assertTrue("Employee's rating is not the same as employee's id", employeeFromDb.getId() == rating);
	}

	@Test
	public void dEmployeeShouldBeUpdated() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_PERSONAL_ID, employeePersonalId);

		employeeFromDb.setAddress("King Street, London");
		employeeDao.createOrUpdate(employeeFromDb);

		Employee updatedEmployee = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_PERSONAL_ID, employeePersonalId);
		assertTrue("Employee's personalId is not updated in db", employeeFromDb.getAddress().equals(updatedEmployee.getAddress()));
	}

	@Test
	public void eEemployeeShouldBeSetAsCompanyBoss() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);

		company.setBoss(employeeFromDb);
		employeeDao.createOrUpdate(employeeFromDb);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		assertTrue("Employee is not successfully set as company boss", employeeFromDb.equals(companyFromDb.getBoss()));
	}

	@Test
	public void fEemployeeShouldBeAssociatedToDepartment() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);

		department.setBoss(employeeFromDb);
		employeeDao.createOrUpdate(employeeFromDb);

		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		assertTrue("Employee is not successfully set as department boss", employeeFromDb.equals(departmentFromDb.getBoss()));
		assertTrue("Department does not contain that employee in the list of employees", departmentFromDb.getEmployees().contains(employeeFromDb));
		assertTrue("Employee's department is not properly set", employeeFromDb.getDepartments().contains(departmentFromDb));
	}

	@Test
	public void gPositionShouldBeSetToEmployee() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);
		assertTrue("Position is not successfully set to employee", position.equals(employeeFromDb.getPosition()));
	}

	@Test
	public void hSameEmployeeShouldNotBeAddedTwiceToCompany() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);

		int sizeBefore = company.getEmployees().size();
		company.addEmployee(employeeFromDb);
		int sizeAfter = company.getEmployees().size();

		assertTrue("Company list of employees can contain a employee more than once", sizeBefore == sizeAfter);
	}

	@Test
	public void iSameEmployeeShouldNotBeAddedTwiceToDepartment() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);

		int sizeBefore = department.getEmployees().size();
		department.addEmployee(employeeFromDb);
		int sizeAfter = department.getEmployees().size();

		assertTrue("Department list of employees can contain a employee more than once", sizeBefore == sizeAfter);
	}

	@Test
	public void jCompanyBossShouldBeUpdatedAndUnset() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail1);

		employeeFromDb.getCompany().setBoss(employeeFromDb);
		employeeDao.createOrUpdate(employeeFromDb);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		assertTrue("Company boss is not properly updated", employeeFromDb.equals(companyFromDb.getBoss()));
		assertTrue("Company does not contain employee in set of employees", companyFromDb.getEmployees().contains(employeeFromDb));

		companyFromDb.setBoss(null);
		employeeDao.createOrUpdate(employeeFromDb);

		companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail1);
		assertNull("Company boss should be null", companyFromDb.getBoss());
		assertTrue("Company does not contain employee in set of employees", companyFromDb.getEmployees().contains(employeeFromDb));
		assertNotNull(employeeFromDb);
	}

	public void kAllEmployeesShouldBeFound() {
		List<Employee> allEmployees = employeeDao.findAll("Employee");

		assertTrue("Employee is not part of the list of all employees", allEmployees.contains(employee));
		assertTrue("Employee is not part of the list of all employees", allEmployees.contains(employee1));
		assertTrue("Employee is not part of the list of all employees", allEmployees.contains(employee2));
		assertTrue("List size is not greater or equal to the number of employees recently added", allEmployees.size() >= 3);
	}

	@Test
	public void lListOfEmployeesShouldBeFoundByNonUniqueParameter() {
		List<Employee> resultList = employeeDao.findByNonUniqueParameter(Constants.SEARCH_BY_AGE, (short) 34);

		assertTrue("This employee is not part of the list", resultList.contains(employee));
		assertTrue("This employee is not part of the list", resultList.contains(employee1));
		assertFalse("This employee is part of the list", resultList.contains(employee2));
		assertTrue("Result list size is not the expected size", resultList.size() >= 2);
	}

	@Test
	public void mListShouldBeFoundByMultipleParameters() {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put(Constants.SEARCH_BY_ADDRESS, "King Street, Manchester");
		parametersMap.put(Constants.SEARCH_BY_AGE, (short) 19);

		List<Employee> resultList = employeeDao.findBySetOfParameters(parametersMap);
		assertFalse("This employee is part of the list", resultList.contains(employee));
		assertFalse("This employee is part of the list", resultList.contains(employee1));
		assertTrue("This employee is not part of the list", resultList.contains(employee2));
		assertTrue("Result list size is not the expected size", resultList.size() >= 1);
	}

	@Test
	public void nCompanyBossShouldBeAddedToEmployeeList() {
		employee3 = new Employee("David Gerrard", (short) 34, "King Street, London", ApplicationUtil.uuidString(), employeeEmail3, "+44 321 213323", new Date(), "url/to/employee/image", company,
				position);

		company.setBoss(employee3);
		employeeDao.createOrUpdate(employee3);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		assertTrue("Company does not contain employee, which is set as a boss", companyFromDb.getEmployees().contains(employee3));
		assertTrue("Employee's company is not properly set", employee3.getCompany().equals(companyFromDb));
	}

	@Test
	public void oCompanyBossShouldBeNullWhenRemovedFromEmployeeList() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail3);

		company.removeEmployee(employeeFromDb);
		employeeDao.createOrUpdate(employeeFromDb);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		assertNull("Company boss is not null", companyFromDb.getBoss());
	}

	@Test
	public void pDepartmentShouldAddEmployeeInCompany() {
		employee3 = new Employee("David Gerrard", (short) 34, "King Street, London", ApplicationUtil.uuidString(), employeeEmail3, "+44 321 213323", new Date(), "url/to/employee/image", company,
				position);

		employeeDao.createOrUpdate(employee3);
		department.setBoss(employee3);
		employeeDao.createOrUpdate(employee3);

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail3);

		assertTrue("Company does not contain employee", companyFromDb.getEmployees().contains(employeeFromDb));
		assertTrue("Department does not contain employee", departmentFromDb.getEmployees().contains(employeeFromDb));
		assertTrue("Employee is not department boss", departmentFromDb.getBoss().equals(employeeFromDb));
		assertTrue("Employee's company is not properly set", employeeFromDb.getCompany().equals(companyFromDb));
		assertTrue("Employee is not added to department", employeeFromDb.getDepartments().contains(departmentFromDb));
	}

	public void qRecordsShouldBeFoundByMultipleIds() {
		List<Long> idList = new ArrayList<Long>();
		idList.add(employee.getId());
		idList.add(employee1.getId());
		idList.add(employee2.getId());

		List<Employee> employees = employeeDao.findMultipleRecordsById(idList);
		assertTrue("Employee is not part of the list", employees.contains(employee));
		assertTrue("Employee is not part of the list", employees.contains(employee1));
		assertTrue("Employee is not part of the list", employees.contains(employee2));
	}

	/*
	 * Following tests should throw exception, because Employee object with non-valid parameters or without any is trying to be saved
	 */

	@Test
	public void rMultipleEmployeesWithSamePersonalIdShouldNotBeSaved() {
		Employee localEmployee = new Employee("Sam James", (short) 34, "King Street, London", employeePersonalId1, ApplicationUtil.uuidString(), "+44 321 213323", new Date(), "url/to/employee/image",
				company, position);

		try {
			employeeDao.createOrUpdate(localEmployee);
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("employee_personal_id_key", e.getConstraintName());
			session.getTransaction().rollback();
			SessionUtil.closeSession(session);
		}
	}

	@Test
	public void sMultipleEmployeesWithSameEmailShouldNotBeSaved() {
		Employee localEmployee = new Employee("Sam James", (short) 34, "King Street, London", ApplicationUtil.uuidString(), employeeEmail, "+44 321 213323", new Date(), "url/to/employee/image",
				company, position);

		try {
			employeeDao.createOrUpdate(localEmployee);
			fail("ConstraintViolationException should have been thrown");
		} catch (ConstraintViolationException e) {
			assertEquals("employee_email_key", e.getConstraintName());
			session.getTransaction().rollback();
			SessionUtil.closeSession(session);
		}
	}

	@Test
	public void tEmptyCompanyShouldNotBeSaved() {
		Employee localEmployee = new Employee();

		try {
			employeeDao.createOrUpdate(localEmployee);
			fail("PropertyValueException should have been thrown");
		} catch (PropertyValueException e) {
			assertEquals("address", e.getPropertyName());
			session.getTransaction().rollback();
			SessionUtil.closeSession(session);
		}
	}

	@Test
	public void uEmployeeEmailLengthConstraintTest() {
		String longEmail = ApplicationUtil.longUuidString().concat(ApplicationUtil.longUuidString());

		Employee localEmployee = new Employee("Mike Obama", (short) 34, "King Street, London", ApplicationUtil.uuidString(), longEmail, "+44 321 213323", new Date(), "url/to/employee/image", company,
				position);
		try {
			employeeDao.createOrUpdate(localEmployee);
			fail("DataException should have been thrown");
		} catch (DataException e) {
			assertTrue(e.getSQLException().getMessage().contains("value too long"));
			session.getTransaction().rollback();
			SessionUtil.closeSession(session);
		}
	}

	@Test
	public void wEmployeeShouldBeDeleted() {
		company = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		department = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);

		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail2);

		company.removeEmployee(employeeFromDb);
		department.removeEmployee(employeeFromDb);
		employeeDao.delete(employeeFromDb);

		Employee deletedEmployee = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail2);
		assertNull("Check if employee was deleted", deletedEmployee);
	}

	@Test
	public void xEmployeeShouldBeDeletedByParentCompany() {
		Employee employeeFromDb = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);

		if (company.getEmployees().isEmpty() && department.getEmployees().isEmpty()) {
			fail("Assert that company and department has employees");
		}
		company.removeEmployee(employeeFromDb);
		department.removeEmployee(employeeFromDb);
		companyDao.createOrUpdate(company);

		Employee deletedEmployee = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail);
		assertNull("Employee was not deleted", deletedEmployee);
	}

	@Test
	public void yMultipleRecordsDeleteShouldRemoveEmployees() {
		employee1 = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail1);
		employee3 = employeeDao.findByUniqueParameter(Constants.SEARCH_BY_EMAIL, employeeEmail3);

		List<Long> idList = new ArrayList<Long>();
		idList.add(employee1.getId());
		idList.add(employee3.getId());

		company.removeEmployee(employee1);
		company.removeEmployee(employee3);

		department.removeEmployee(employee1);
		department.removeEmployee(employee3);

		employeeDao.deleteMultipleRecords("Employee", idList);

		List<Employee> employees = employeeDao.findMultipleRecordsById(idList);
		assertFalse("Employee is not successfully deleted", employees.contains(employee1));
		assertFalse("Employee is not successfully deleted", employees.contains(employee3));

		Company companyFromDb = companyDao.findByUniqueParameter(Constants.SEARCH_BY_BULSTAT, companyBulstat);
		Department departmentFromDb = departmentDao.findByUniqueParameter(Constants.SEARCH_BY_ID, departmentId);

		assertNotNull("Company does not exist", companyFromDb);
		assertNotNull("Department does not exist", departmentFromDb);

		assertNull("Company boss is not null", companyFromDb.getBoss());
		assertNull("Department boss is not null", departmentFromDb.getBoss());

		assertFalse("Company list of employees contains that employee", companyFromDb.getEmployees().contains(employee1));
		assertFalse("Department list of employees contains that employee", departmentFromDb.getEmployees().contains(employee1));

		assertFalse("Company list of employees contains that employee", companyFromDb.getEmployees().contains(employee3));
		assertFalse("Department list of employees contains that employee", departmentFromDb.getEmployees().contains(employee3));
	}

	@Test
	public void zCompanySholdClearAllAssociatedEntities() {
		companyDao.delete(company);

		List<Long> idList = new ArrayList<Long>();
		idList.add(employee.getId());
		idList.add(employee1.getId());
		idList.add(employee2.getId());
		idList.add(employee3.getId());

		List<Employee> employees = employeeDao.findMultipleRecordsById(idList);

		assertFalse("Employee is not successfully deleted", employees.contains(employee));
		assertFalse("Employee is not successfully deleted", employees.contains(employee1));
		assertFalse("Employee is not successfully deleted", employees.contains(employee2));
		assertFalse("Employee is not successfully deleted", employees.contains(employee3));
	}

	@After
	public void commitTransaction() {
		if (session.isOpen() && session.getTransaction().isActive() && !session.getTransaction().wasRolledBack()) {
			session.getTransaction().commit();
		}
	}

	@AfterClass
	public static void closeSession() {
		SessionUtil.closeSession(session);
	}

}