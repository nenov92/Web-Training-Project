<%@ page import="java.util.List"%>
<%@ page import="com.example.entities.Employee"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="comapnySelectedForEdit" class="com.example.entities.Company" scope="request" />

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Edit Company</title>
		
		<link rel="stylesheet" type="text/css" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<%@include file="navigation.jsp" %>
		<br />
		<h1 class="container-header">Edit Company</h1>
		<div class="container">
			<form action="create" method="post" enctype="multipart/form-data" onsubmit="return formIsValidForSaving()">
			    <div class="table-row">
			        <div class="col-left">Name:</div>
			        <div class="col-right">
			        	<input type="text" name="companyName" readonly="readonly" value="<%= comapnySelectedForEdit.getName() %>" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="address">Address:</div>
			        <div class="col-right">
			            <input type="text" name="companyAddress" id="companyAddress" value="<%= comapnySelectedForEdit.getAddress() %>"  maxlength="100" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="establishedDate">Established Date:</div>
			        <div class="col-right">
			            <input type="date" name="companyEstablishedDate" id="companyEstablishedDate" value="<%=comapnySelectedForEdit.getFormattedEstablishedDate() %>" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left">Bulstat:</div>
			        <div class="col-right">
			            <input type="text" name="companyBulstat" readonly="readonly" value="<%= comapnySelectedForEdit.getBulstat() %>" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="logo">Logo:</div>
			        <div class="col-right">
			            <img id="image" src="/MyWebProjectStaticContent/<%= comapnySelectedForEdit.getLogo() %>" alt="Company Logo" />
			            <input type="file" name="companyLogo" id="companyLogo" />
			        </div>
	    		</div>
	    		<div class="table-row">
			        <div class="col-left">Company Boss:</div>
			        <div class="col-right">
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
			        </div>
	    		</div>
				<div class="table-row">
			        <div class="col-left">
						<input type="submit" value="Save" />
						<input type="button" value="Cancel" onclick="window.location.replace(document.referrer)" />
			        </div>
		    	</div>
			</form>
		</div>
		<div id="myModal" class="modal">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title">Alert</h4>
		            </div>
		            <div class="modal-body">
		                <p>Address, Established Date and Logo must be provided!</p>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">Okay</button>
		            </div>
		        </div>
		    </div>
		</div>
		<script>
			var	addressIsValid = true,
				establishedDateIsValid = true;
				
			
			function openPopUp() {
			    $("#myModal").modal('show');
			}
			
			$(document).ready(function () {
			    $("#myModal").modal('hide');
			});
			
			$("#companyAddress").blur(function() {
				if($('#companyAddress').val() == "" || $('#companyAddress').val() == null){
					if(!($("#address").html().indexOf("*")>=0)){
						$("#address").append("<span id=\"addressSpan\">*</span>");
						$("#address").css("color", "red");
					}
					addressIsValid = false;
				} else {
					$("#address").children("#addressSpan").remove();
					$("#address").css("color", "#000");
					addressIsValid = true;
				}
			});
			
			$("#companyEstablishedDate").blur(function() {
				if($('#companyEstablishedDate').val() == "" || $('#companyEstablishedDate').val() == null){
					if(!($("#establishedDate").html().indexOf("*")>=0)){
						$("#establishedDate").append("<span id=\"establishedDateSpan\">*</span>");
						$("#establishedDate").css("color", "red");
					}
					establishedDateIsValid = false;
				} else {
					$("#establishedDate").children("#establishedDateSpan").remove();
					$("#establishedDate").css("color", "#000");
					establishedDateIsValid = true;
				}
			});
			
			function formIsValidForSaving() {
				if(addressIsValid && establishedDateIsValid){
					return true;
				}
				openPopUp();
				return false;
			};
		</script>			
	</body>
</html>