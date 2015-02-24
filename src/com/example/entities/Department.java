package com.example.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.example.utils.Constants;

@Entity
@Table(name = "department", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "company_id" }), @UniqueConstraint(columnNames = "rating") })
public class Department implements Serializable{
	private static final long serialVersionUID = 8941653417514913803L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "logo", length = 100, nullable = true)
	private String logo;

	@Column(name = "rating", nullable = false)
	@Generated(GenerationTime.INSERT)
	private long rating;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.department")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<DepartmentEmployee> departmentEmployees = new ArrayList<DepartmentEmployee>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boss_id")
	private Employee boss;

	public Department() {
	}

	public Department(String name, String description, String logo, Company company) {
		this.setName(name);
		this.setDescription(description);
		this.setLogo(logo);
		this.setRating(rating);
		this.setCompany(company);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Employee getBoss() {
		return boss;
	}

	public void setBoss(Employee boss) {
		this.boss = boss;
		addEmployee(boss);
		this.company.addEmployee(boss);
	}

	public List<DepartmentEmployee> getDepartmentEmployees() {
		return departmentEmployees;
	}

	public void setDepartmentEmployees(List<DepartmentEmployee> departmentEmployees) {
		this.departmentEmployees = departmentEmployees;
	}

	public void addDepartmentEmployees(DepartmentEmployee departmentEmployees) {
		this.departmentEmployees.add(departmentEmployees);
	}

	public void removeDepartmentEmployees(DepartmentEmployee departmentEmployees) {
		this.departmentEmployees.remove(departmentEmployees);
	}

	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		for (DepartmentEmployee de : this.departmentEmployees) {
			employees.add(de.getEmployee());
		}
		return employees;
	}

	public void addEmployee(Employee employee) {
		DepartmentEmployee de = new DepartmentEmployee();
		de.setDepartment(this);
		de.setEmployee(employee);
		if (!this.departmentEmployees.contains(de)) {
			this.getDepartmentEmployees().add(de);
			employee.getDepartmentEmployees().add(de);
			de.setRating(employee.getId());
		}

		this.company.addEmployee(employee);
	}

	public void removeEmployee(Employee employee) {
		Iterator<DepartmentEmployee> it = this.departmentEmployees.iterator();
		while (it.hasNext()) {
			DepartmentEmployee de = it.next();
			if (de.getEmployee().equals(employee)) {
				it.remove();
			}
		}
		
		if (this.boss != null && this.boss.equals(employee)) {
			this.boss = null;
		}
	}

	public List<Employee> getEmployeesSuitableForBoss() {
		List<Employee> suitableEmployees = new ArrayList<Employee>();
		for (Employee e : this.getEmployees()) {
			if (e.getPosition().getLevel() == Constants.DEPARTMENT_BOSS_LEVEL) {
				suitableEmployees.add(e);
			}
		}
		return suitableEmployees;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Department) {
			Department other = (Department) o;
			if (this.getId() == other.getId() && this.getCompany().equals(other.getCompany()) && this.getName().equals(other.getName()) && this.getRating() == other.getRating()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.id + this.rating);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ");
		sb.append(getId());
		sb.append(", Name: ");
		sb.append(getName());
		sb.append(", Description: ");
		sb.append(getDescription());
		sb.append(", Logo:");
		sb.append(getLogo());
		sb.append(", Rating: ");
		sb.append(getRating());
		return sb.toString();
	}

}