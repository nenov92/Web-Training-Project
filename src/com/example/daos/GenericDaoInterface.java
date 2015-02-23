package com.example.daos;

import java.util.HashMap;
import java.util.List;

public interface GenericDaoInterface<T> {

	public void createOrUpdate(T object);

	public void delete(T object);
	
	void deleteMultipleRecords(String tablename, List<Long> identifiers);
	
	public List<T> findAll(String tableName);

	public T findByUniqueParameter(String paramName, Object paramValue);
	
	public List<T> findByNonUniqueParameter(String paramName, Object paramValue);

	public List<T> findBySetOfParameters(HashMap<String, Object> nameValueMap);

	public List<T> findMultipleRecordsById(List<Long> identifiers);

	public List<T> findTop(int numberOfElements);
}