package com.example.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "position")
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "level", nullable = false)
	private short level;

	public Position() {
	}

	public Position(String name, short level) {
		setName(name);
		setLevel(level);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Position) {
			Position other = (Position) o;
			if (this.getId() == other.getId()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.id);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ");
		sb.append(getId());
		sb.append(", Name: ");
		sb.append(getName());
		sb.append(", Level: ");
		sb.append(getLevel());
		return sb.toString();
	}

}