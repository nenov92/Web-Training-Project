package com.example.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Embeddable
public class DepartmentEmployeeId implements Serializable {

	@ManyToOne
	private Department department;

	@ManyToOne
	private Employee employee;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof DepartmentEmployeeId) {
			DepartmentEmployeeId other = (DepartmentEmployeeId) o;
			if (this.getDepartment().equals(other.getDepartment()) && this.getEmployee().equals(other.getEmployee())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.department.hashCode() + this.employee.hashCode());
		return result;
	}

}