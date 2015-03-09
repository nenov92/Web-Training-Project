<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.entities.Employee"%>

<jsp:useBean id="comapnySelectedForEdit" class="com.example.entities.Company" scope="request" />

<!DOCTYPE html>
<html>
	<head>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:choose>
		    <c:when test="${comapnySelectedForEdit.getName() != null}">
				<c:set var="isCreate" value="false" scope="page" />
		    </c:when>
		    <c:otherwise>
		       <c:set var="isCreate" value="true" scope="page" />
		    </c:otherwise>
		</c:choose>
		
		<c:choose>
		    <c:when test="${isCreate}">
		       <title>Create Company</title>
		    </c:when>
		    <c:otherwise>
		       <title>Edit Company</title>
		    </c:otherwise>
		</c:choose>
		
		<meta charset="UTF-8">
		
		<link rel="stylesheet" type="text/css" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</head>
	<%@include file="navigation.jsp" %>
		<div class="divTable">
			<c:choose>
			    <c:when test="${isCreate}">
					<h1 class="container-header">Fill in Company Details</h1>
			    </c:when>
			    <c:otherwise>
					<h1 class="container-header">Edit Company</h1>
			    </c:otherwise>
			</c:choose>
			<div class="container">
				<form action="create" method="post" enctype="multipart/form-data" onsubmit="return formIsValidForSaving()">
				    <div class="table-row">
				        <div class="col-left" id="name">Name:</div>
				        <div class="col-right" id="companyNameInput">
				        	<c:choose>
							    <c:when test="${isCreate}">
							       <input type="text" name="companyName" id="companyName" maxlength="50" required />
							    </c:when>
							    <c:otherwise>
							       <input type="text" name="companyName" readonly="readonly" value="<%= comapnySelectedForEdit.getName() %>" />
							    </c:otherwise>
							</c:choose>
				        </div>
				    </div>
				    <div class="table-row">
				        <div class="col-left" id="address">Address:</div>
				        <div class="col-right">
					        <c:choose>
					        	<c:when test="${isCreate}">
									 <input type="text" name="companyAddress" id="companyAddress" maxlength="100" required />
								</c:when>
								<c:otherwise>
									<input type="text" name="companyAddress" id="companyAddress" value="<%= comapnySelectedForEdit.getAddress() %>"  maxlength="100" required />
								</c:otherwise>
							</c:choose>
				        </div>
				    </div>
				    <div class="table-row">
				        <div class="col-left" id="establishedDate">Established Date:</div>
				        <div class="col-right">
				        	<c:choose>
					        	<c:when test="${isCreate}">
									<input type="date" name="companyEstablishedDate" id="companyEstablishedDate" required />
								</c:when>
								<c:otherwise>
									<input type="date" name="companyEstablishedDate" id="companyEstablishedDate" value="<%=comapnySelectedForEdit.getFormattedEstablishedDate() %>" required />
								</c:otherwise>
				        	</c:choose>
				        </div>
				    </div>
				    <div class="table-row">
				       	<div class="col-left" id="bulstat">Bulstat:</div>
				        <div class="col-right" id="companyBulstatInput">
					        <c:choose>
					        	<c:when test="${isCreate}">
					        		<input type="text" name="companyBulstat" id="companyBulstat" maxlength="50" required />
								</c:when>
								<c:otherwise>
									<input type="text" name="companyBulstat" readonly="readonly" value="<%= comapnySelectedForEdit.getBulstat() %>" />
								</c:otherwise>
					        </c:choose>
				        </div>
				    </div>
				    <div class="table-row">
				        <div class="col-left" id="logo">Logo:</div>
				        <div class="col-right">
					        <c:choose>
					        	<c:when test="${isCreate}">
					        		<input type="file" name="companyLogo" id="companyLogo" onchange="validateLogo()" required />
								</c:when>
								<c:otherwise>
						            <img id="image" src="/MyWebProjectStaticContent/<%= comapnySelectedForEdit.getLogo() %>" alt="Company Logo" />
						            <input type="file" name="companyLogo" id="companyLogo" />
								</c:otherwise>
					        </c:choose>
				        </div>
		    		</div>
		    		<div class="table-row">
				        <div class="col-left">Company Boss:</div>
				        <div class="col-right">
				        	<c:choose>
					        	<c:when test="${isCreate}">
					        		<select name="companyBoss" disabled></select>
								</c:when>
								<c:otherwise>
						        	<% if(comapnySelectedForEdit.getEmployeesSuitableForBoss().size() > 0){ %>
							            <select name="companyBoss">
							            	<% List<Employee> suitableEmployees = comapnySelectedForEdit.getEmployeesSuitableForBoss();
							            	   for (Employee e : suitableEmployees){ 
							            	   		if(comapnySelectedForEdit.getBoss().equals(e)){ %>
							            		   		<option selected value="<%= e.getId() %>"><%=e.getName() %></option>
							            	   		<% } else { %>
							            	   			<option value="<%= e.getId() %>"><%=e.getName() %></option>
							            	   		<% } %>
							            	<% } %>
							            </select>
						        	<% } else { %>
						        		<select name="companyBoss" disabled></select>
						        	<% } %>	
								</c:otherwise>
				        	</c:choose>
				        </div>
		    		</div>
					<div class="table-row">
			        	<c:choose>
				        	<c:when test="${isCreate}">
				        		<div class="col-left">
									<input type="submit" value="Create Company" id="createCompany"/>
							   	</div>
							    <div class="col-right">
						    		<input type="button" value="Back" onclick="window.location.replace(document.referrer)" />
						  		</div>
							</c:when>
							<c:otherwise>
						        <div class="col-left">
									<input type="submit" value="Save" />
									<input type="button" value="Cancel" onclick="window.location.replace(document.referrer)" />
						        </div>
							</c:otherwise>
				        </c:choose>
			    	</div>
				</form>
			</div>
		</div>
		<div id="myModal" class="modal">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title">Alert</h4>
		            </div>
		            <div class="modal-body">
		                <c:choose>
			                <c:when test="${isCreate}">
								 <p>Name and bulstat fields should have unique values!</p>
							</c:when>
							<c:otherwise>
				                <p>Address, Established Date and Logo must be provided!</p>
							</c:otherwise>
				        </c:choose>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">Okay</button>
		            </div>
		        </div>
		    </div>
		</div>
		<c:choose>
			<c:when test="${isCreate}">
				<div class="loadingIndicator"></div>
				<script src="js/createCompany.js"></script>
			</c:when>
			<c:otherwise>
				<script src="js/editCompany.js"></script>
			</c:otherwise>
		</c:choose>
	</body>
</html>