package com.example.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CompanyTest.class, DepartmentTest.class, EmployeeTest.class })
public class AllTests {

}