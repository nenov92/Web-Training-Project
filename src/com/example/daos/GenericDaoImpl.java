package com.example.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class GenericDaoImpl<T> implements GenericDaoInterface<T> {

	private Session currentSession;
	private Class<T> entityClass;

	public GenericDaoImpl() {
	}

	public GenericDaoImpl(Session globalSession, Class<T> entityClass) {
		setCurrentSession(globalSession);
		this.entityClass = entityClass;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	@Override
	public void createOrUpdate(T object) {
		getCurrentSession().saveOrUpdate(object);
	}

	@Override
	public void delete(T object) {
		getCurrentSession().delete(object);
	}

	@Override
	public void deleteMultipleRecords(String tablename, List<Long> identifiers) {
		Query query = getCurrentSession().createQuery("DELETE FROM " + tablename + " o WHERE o.id IN (:list)");
		query.setParameterList("list", identifiers);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(String tableName) {
		return (List<T>) getCurrentSession().createQuery("from " + tableName).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findByUniqueParameter(String paramName, Object paramValue) {
		Criteria criteria = getCurrentSession().createCriteria(this.entityClass).add(Restrictions.eq(paramName, paramValue));
		return (T) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNonUniqueParameter(String paramName, Object paramValue) {
		Criteria criteria = getCurrentSession().createCriteria(this.entityClass).add(Restrictions.eq(paramName, paramValue));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findBySetOfParameters(HashMap<String, Object> nameValueMap) {
		Criteria criteria = getCurrentSession().createCriteria(this.entityClass);

		for (Entry<String, Object> entry : nameValueMap.entrySet()) {
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
		}

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findMultipleRecordsById(List<Long> identifiers) {
		Criteria criteria = getCurrentSession().createCriteria(this.entityClass).add(Restrictions.in("id", identifiers));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findTop(int numberOfElements) {
		Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
		criteria.addOrder(Order.asc("rating"));
		criteria.setMaxResults(numberOfElements);
		return criteria.list();
	}
	
}