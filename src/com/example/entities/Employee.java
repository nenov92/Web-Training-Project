package com.example.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "employee", uniqueConstraints = { @UniqueConstraint(columnNames = "personal_id"), @UniqueConstraint(columnNames = "email") })
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "age", nullable = false)
	private short age;

	@Column(name = "address", length = 150, nullable = false)
	private String address;

	@Column(name = "personal_id", length = 50, nullable = false)
	private String personalId;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "telephone", length = 20, nullable = false)
	private String telephone;

	@Column(name = "hire_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date hireDate;

	@Column(name = "end_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "image", length = 100, nullable = false)
	private String image;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToOne(mappedBy = "boss")
	private Company companyBoss;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.employee")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<DepartmentEmployee> departmentEmployees = new ArrayList<DepartmentEmployee>();

	@OneToOne(mappedBy = "boss")
	private Department departmentBoss;

	@ManyToOne
	@JoinColumn(name = "position_id")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private Position position;

	public Employee() {
	}

	public Employee(String name, short age, String address, String personalId, String email, String telephone, Date hireDate, String image, Company company, Position position) {
		setName(name);
		setAge(age);
		setAddress(address);
		this.personalId = personalId;
		this.email = email;
		setTelephone(telephone);
		setHireDate(hireDate);
		setImage(image);
		setCompany(company);
		setPosition(position);
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

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPersonalId() {
		return personalId;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
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

	public List<Department> getDepartments() {
		List<Department> departments = new ArrayList<Department>();
		for (DepartmentEmployee de : this.departmentEmployees) {
			departments.add(de.getDepartment());
		}
		return departments;
	}

	public void addDepartment(Department department) {
		DepartmentEmployee de = new DepartmentEmployee();
		de.setEmployee(this);
		de.setDepartment(department);
		if (!this.departmentEmployees.contains(de)) {
			this.getDepartmentEmployees().add(de);
			department.getDepartmentEmployees().add(de);
			de.setRating(this.getId());
		}
	}

	public void removeDepartment(Department department) {
		Iterator<DepartmentEmployee> it = this.departmentEmployees.iterator();
		while (it.hasNext()) {
			DepartmentEmployee de = it.next();
			if (de.getDepartment().equals(department)) {
				it.remove();
			}
		}
	}

	public Company getCompanyBoss() {
		return companyBoss;
	}

	public void setCompanyBoss(Company companyBoss) {
		this.companyBoss = companyBoss;
	}

	public Department getDepartmentBoss() {
		return departmentBoss;
	}

	public void setDepartmentBoss(Department departmentBoss) {
		this.departmentBoss = departmentBoss;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Employee) {
			Employee other = (Employee) o;
			if (this.getId() == other.getId() && this.getPersonalId().equals(other.getPersonalId()) && this.getEmail().equals(other.getEmail())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.id + this.personalId.hashCode() + this.email.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ");
		sb.append(getId());
		sb.append(", Name: ");
		sb.append(getName());
		sb.append(", Age: ");
		sb.append(getAge());
		sb.append(", Address: ");
		sb.append(getAddress());
		sb.append(", Personal ID: ");
		sb.append(getPersonalId());
		sb.append(", Email: ");
		sb.append(getEmail());
		sb.append(", Telephone: ");
		sb.append(getTelephone());
		sb.append(", Hire date: ");
		sb.append(new SimpleDateFormat("dd.MM.yyyy").format(getHireDate()));
		sb.append(", End date: ");
		sb.append(getEndDate() != null ? new SimpleDateFormat("dd.MM.yyyy").format(getEndDate()) : "No end date available");
		sb.append(", Image:");
		sb.append(getImage());
		return sb.toString();
	}

}