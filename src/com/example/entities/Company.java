package com.example.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.example.utils.Constants;

@Entity
@Table(name = "company", uniqueConstraints = { @UniqueConstraint(columnNames = "name"), @UniqueConstraint(columnNames = "bulstat"), @UniqueConstraint(columnNames = "rating") })
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "address", length = 100, nullable = false)
	private String address;

	@Column(name = "established_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date establishedDate;

	@Column(name = "bulstat", length = 50, nullable = false)
	private String bulstat;

	@Column(name = "logo", length = 100, nullable = true)
	private String logo;

	@Column(name = "rating", nullable = false)
	@Generated(GenerationTime.INSERT)
	private long rating;

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<Department> departments = new ArrayList<Department>();

	@OneToMany(mappedBy = "company", fetch = FetchType.EAGER, orphanRemoval = true)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<Employee> employees = new ArrayList<Employee>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "boss_id")
	private Employee boss;

	public Company() {
	}

	public Company(String name, String address, Date establishedDate, String bulstat, String logo) {
		this.name = name;
		setAddress(address);
		setEstablishedDate(establishedDate);
		this.bulstat = bulstat;
		setLogo(logo);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getEstablishedDate() {
		return establishedDate;
	}

	public String getFormattedEstablishedDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(getEstablishedDate());
	}

	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}

	public String getBulstat() {
		return bulstat;
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

	public List<Department> getDepartments() {
		return departments;
	}

	public void addDepartment(Department department) {
		if (!(this.departments.contains(department))) {
			this.departments.add(department);
		}
	}

	public void removeDepartment(Department department) {
		if (this.departments.contains(department)) {
			this.departments.remove(department);
		}
	}

	public void removeDepartments(List<Department> departments) {
		this.departments.removeAll(departments);
	}

	public void removeDepartments() {
		this.departments.clear();
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void addEmployee(Employee employee) {
		if (!(employees.contains(employee))) {
			this.employees.add(employee);
		}
	}

	public void addEmployees(List<Employee> employees) {
		this.employees.addAll(employees);
	}

	public void removeEmployee(Employee employee) {
		if (this.employees.contains(employee)) {
			this.employees.remove(employee);
		}
		if (this.boss != null && this.boss.equals(employee)) {
			this.boss = null;
		}
	}

	public void removeEmployees(List<Employee> employees) {
		this.employees.removeAll(employees);
	}

	public void removeEmployees() {
		this.employees.clear();
	}

	public Employee getBoss() {
		return boss;
	}

	public void setBoss(Employee boss) {
		this.boss = boss;
		addEmployee(boss);
	}

	public List<Employee> getEmployeesSuitableForBoss() {
		List<Employee> suitableEmployees = new ArrayList<Employee>();
		for (Employee e : this.employees) {
			if (e.getPosition().getLevel() == Constants.COMPANY_BOSS_LEVEL) {
				suitableEmployees.add(e);
			}
		}
		return suitableEmployees;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Company) {
			Company other = (Company) o;
			if (this.getId() == other.getId() && this.getName().equals(other.getName()) && this.getBulstat().equals(other.getBulstat())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.id + this.name.hashCode() + this.bulstat.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ");
		sb.append(getId());
		sb.append(", Name: ");
		sb.append(getName());
		sb.append(", Address: ");
		sb.append(getAddress());
		sb.append(", Established date: ");
		sb.append(new SimpleDateFormat("dd.MM.yyyy").format(getEstablishedDate()));
		sb.append(", Bulstat: ");
		sb.append(getBulstat());
		sb.append(", Logo:");
		sb.append(getLogo());
		sb.append(", Rating: ");
		sb.append(getRating());
		return sb.toString();
	}

}