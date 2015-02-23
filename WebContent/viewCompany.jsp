<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<jsp:useBean id="comapnySelectedForView" class="com.example.entities.Company" scope="request" />

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>View Company</title>
		
		<link rel="stylesheet" type="text/css" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
	</head>
	<body>
		<%@include file="navigation.jsp" %>
		<br />
		<h1 class="container-header">Company Details</h1>
		<div class="container">
		    <div class="table-row">
		        <div class="col-left">Name:</div>
		        <div class="col-right">
		        	<input type="text" name="companyName" disabled value="<%= comapnySelectedForView.getName() %>">
		        </div>
		    </div>
		    <div class="table-row">
		        <div class="col-left">Address:</div>
		        <div class="col-right">
		            <input type="text" name="companyAddress" disabled value="<%= comapnySelectedForView.getAddress() %>">
		        </div>
		    </div>
		    <div class="table-row">
		        <div class="col-left">Established Date:</div>
		        <div class="col-right">
		            <input type="date" name="companyEstablishedDate" disabled value="<%=comapnySelectedForView.getFormattedEstablishedDate() %>">
		        </div>
		    </div>
		    <div class="table-row">
		        <div class="col-left">Bulstat:</div>
		        <div class="col-right">
		            <input type="text" name="companyBulstat" disabled value="<%= comapnySelectedForView.getBulstat() %>">
		        </div>
		    </div>
		    <div class="table-row">
		        <div class="col-left">Logo:</div>
		        <div class="col-right">
		            <img id="image" src="/MyWebProjectStaticContent/<%= comapnySelectedForView.getLogo() %>" alt="Company Logo">
		        </div>
    		</div>
    		<div class="table-row">
				<div class="col-left">Company Boss:</div>
			    <div class="col-right">
			    	<select name="companyBoss" disabled>
			        	<option><%=comapnySelectedForView.getBoss() != null ? comapnySelectedForView.getBoss().getName() : "" %><option>
					</select>
				</div>
	    	</div>
	    	<div class="table-row">
				<div class="col-left">
					<input type="button" value="Back" onclick="window.location.replace(document.referrer)" />
				</div>
	    	</div>
		</div>
	</body>
</html>