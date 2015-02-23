package com.example.entities;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "department_employee", uniqueConstraints = { @UniqueConstraint(columnNames = { "department_id", "rating" }) })
@AssociationOverrides({ @AssociationOverride(name = "pk.department", joinColumns = @JoinColumn(name = "department_id")),
		@AssociationOverride(name = "pk.employee", joinColumns = @JoinColumn(name = "employee_id")) })
public class DepartmentEmployee {

	@EmbeddedId
	private DepartmentEmployeeId pk = new DepartmentEmployeeId();

	@Column(name = "rating", nullable = false)
	private long rating;

	public DepartmentEmployee() {
	}

	public DepartmentEmployeeId getPk() {
		return pk;
	}

	public void setPk(DepartmentEmployeeId pk) {
		this.pk = pk;
	}

	@Transient
	public Department getDepartment() {
		return getPk().getDepartment();
	}

	public void setDepartment(Department department) {
		getPk().setDepartment(department);
	}

	@Transient
	public Employee getEmployee() {
		return getPk().getEmployee();
	}

	public void setEmployee(Employee employee) {
		getPk().setEmployee(employee);
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof DepartmentEmployee) {
			DepartmentEmployee other = (DepartmentEmployee) o;
			if (this.getPk().equals(other.getPk())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.pk.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Department: ");
		sb.append(getDepartment());
		sb.append(", Employee: ");
		sb.append(getEmployee());
		sb.append(", Rating: ");
		sb.append(getRating());
		return sb.toString();
	}

}