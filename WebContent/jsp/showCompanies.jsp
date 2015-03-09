<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.example.entities.Company"%>
<%@ page import="com.example.utils.Constants"%>

<jsp:useBean id="companiesFromDb" class="java.util.ArrayList" scope="session" />

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Companies</title>
		
		<link rel="stylesheet" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<%@include file="navigation.jsp" %>
		<%
		if (session.getAttribute("userNotification") != null) {
			String result = (String) session.getAttribute("userNotification");
			
			if (result.equals(Constants.UNSUCCESSFUL_OUTCOME)) {
				%>
				<div id="userNotification">
    				<div class="alert alert-danger alert-error">
        				<a href="#" class="close" data-dismiss="alert">&times;</a>
        				<strong>Error!</strong> Operation was unsuccessful!
		    		</div>
				</div>
				<%
			} else if (result.equals(Constants.SUCCESSFUL_CREATE)) {
				%>
				<div id="userNotification">
    				<div class="alert alert-success">
        				<a href="#" class="close" data-dismiss="alert">&times;</a>
        				<strong>Success!</strong> Company was successfully created!
		    		</div>
				</div>
				<%
			} else if (result.equals(Constants.SUCCESSFUL_EDIT)) {
				%>
				<div id="userNotification">
    				<div class="alert alert-success">
        				<a href="#" class="close" data-dismiss="alert">&times;</a>
        				<strong>Success!</strong> Company was successfully updated!
		    		</div>
				</div>
				<%
			} else if (result.equals(Constants.SUCCESSFUL_REMOVE)) {
				%>
				<div id="userNotification">
    				<div class="alert alert-success">
        				<a href="#" class="close" data-dismiss="alert">&times;</a>
        				<strong>Success!</strong> Company was successfully removed!
		    		</div>
				</div>
				<%
			}
			
			session.removeAttribute("userNotification");
		}
		%>
		<div class="grid-dimension">
			<p>Please select grid dimensions:</p>
			<form action="companies" method="post">
				<input type="number" class="grid-dimension-input" id="x-axis" name="x-axis" value="3" min="1" max="5" />
				<span id="grid-dimension-span">x</span>
				<input type="number" class="grid-dimension-input" id="y-axis" name="y-axis" value="3" min="1" max="5" />
		  		<input type="submit" value="Apply"/>
	  		</form>
		</div>
		<div class="divTable" id="divTable">
	    	<div class="divRow" id="divRow">
					<%
						int xValue;
						int yValue;
						// TODO use list from servlet
						List<Company> companiesForDisplay = new ArrayList<Company>(companiesFromDb);

						// use only values from servlet
						if (session.getAttribute("yValue") != null) {
							yValue = Integer.parseInt(session.getAttribute("yValue").toString());
						} else {
							yValue = 3;
						}

						if (session.getAttribute("xValue") != null) {
							xValue = Integer.parseInt(session.getAttribute("xValue").toString());
						} else {
							xValue = 3;
						}

						for (int i = 0; i < companiesForDisplay.size(); i++) {
							// TODO remove
							if (companiesForDisplay.get(i) == null) {
								continue;
							}
							if ((i != 0) && (i % yValue == 0)) {
					%>
			</div>
	    	<div class="divRow">
						<%	}%>
				<div class="divCell" id="<%=i%>">
					<button id="northBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) - <%=yValue%>, swapInnerContentTop)">&#94;</button>
           			<button id="eastBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) + 1, swapInnerContentRight)">&#62;</button>
           			<button id="westBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) - 1, swapInnerContentLeft)">&#60;</button>
                   	<div class="cellInner" id="cellInner">
						<div class="cellContent">
							<label><%=companiesForDisplay.get(i).getName()%></label>
							<br />
							<img src="/MyWebProjectStaticContent/<%=companiesForDisplay.get(i).getLogo()%>" alt="Company Logo">
							<input type="hidden" id="companyId" name="companyId" value="<%=((Company) companiesForDisplay.get(i)).getId()%>" />
						</div>
						<div class="cellNavigation">
	            	    	<%-- TODO merge to one form & use only one hidden field with comp id --%>
	            	    	<form>
	            	    		<input type="hidden" name="" value="" />
	            	    		<input disabled class="submit-button" type="submit" value="View&#x00A;Departments" />
	            	    	</form>
	            	    	<form action="editcompany" method="post">
	            	    		<input type="hidden" name="companyEditId" value="<%=((Company) companiesForDisplay.get(i)).getId()%>" />
	            	    		<input class="submit-button" type="submit" value="Edit" />
	            	    	</form>
	            	    	<form action="viewcompany" method="post">
	            	    		<input type="hidden" name="viewId" value="<%=((Company) companiesForDisplay.get(i)).getId()%>" />
	            	    		<input class="submit-button" type="submit" value="View" />
	            	    	</form>
	            	    	<%-- change openPopUp naming --%>
	            	    	<input class="submit-button" type="button" value="Remove" onclick="openPopUp(<%=((Company) companiesForDisplay.get(i)).getId()%>)" />
						</div>
           			</div>
					<button id="southBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) + <%=yValue%>, swapInnerContentBottom)">v</button>
				</div>
						<%	}%>
			</div>
		</div>
		<%-- hide with CSS --%>
		<div id="myModal" class="modal">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title">Confirmation</h4>
		            </div>
		            <div class="modal-body">
		                <p>Are you sure you want to delete this company?</p>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		                <form class="display-inline" id="removeForm" action="removecompany" method="post">
	            	    	<input type="hidden" id="removeId" name="companyRemoveId" value="">
	            	    	<input class="btn btn-primary" type="submit" value="Remove" onclick="removeCompany()">
	            	    </form>
		            </div>
		        </div>
		    </div>
		</div>
		<script src="js/showCompanies.js"></script>
		<script>
			// in grid dimensions set current x and y values
			$(document).ready(setX(<%=xValue%>));
			$(document).ready(setY(<%=yValue%>));
		</script>
	</body>
</html>