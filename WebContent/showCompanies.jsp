<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.example.entities.Company"%>

<jsp:useBean id="companiesInitial" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="companiesFromUserInput" class="java.util.ArrayList" scope="request" />

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Companies</title>
		
		<link rel="stylesheet" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<%@include file="navigation.jsp" %>
		<br />
		<div class="grid-dimension">
			<p>Please select grid dimensions:</p>
			<form action="companies" method="post">
				<input type="number" class="grid-dimension-input" name="x-axis" value="3" min="1" max="5" />x
				<input type="number" class="grid-dimension-input" name="y-axis" value="3" min="1" max="5" />
		  		<input type="submit" value="Apply"/>
	  		</form>
		</div>
		<br />
		<div class="divTable" id="divTable">
	    	<div class="divRow" id="divRow">
					<%
						int yValue;
						List<Company> companiesForDisplay;

						if (request.getAttribute("yValue") != null) {
							yValue = Integer.parseInt(request.getAttribute("yValue").toString());
						} else {
							yValue = 3;
						}

						if (companiesFromUserInput.isEmpty()) {
							companiesForDisplay = new ArrayList<Company>(companiesInitial);
						} else {
							companiesForDisplay = new ArrayList<Company>(companiesFromUserInput);
						}

						for (int i = 0; i < companiesForDisplay.size(); i++) {
							if (i % yValue == 0) {
					%>
			</div>
	    	<div class="divRow">
					<%
							}
					%>
					<div class="divCell" id="<%=i%>">
						<button id="northBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) - <%=yValue%>, swapInnerContentTop)">^</button>
            			<button id="eastBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) + 1, swapInnerContentRight)">></button>
            			<button id="westBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) - 1, swapInnerContentLeft)"><</button>
                    	<div class="cellInner" id="cellInner">
							<div class="cellContent">
								<label><%=companiesForDisplay.get(i).getName()%></label>
								<br />
								<img src="/MyWebProjectStaticContent/<%=companiesForDisplay.get(i).getLogo()%>" alt="Company Logo">
								<input type="hidden" name="companyId" value="<%=((Company) companiesForDisplay.get(i)).getId()%>" />
							</div>
							<div class="cellNavigation">
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
		            	    	<input class="submit-button" type="button" value="Remove" onclick="openPopUp(<%=((Company) companiesForDisplay.get(i)).getId()%>)" />
							</div>
            			</div>
						<button id="southBtn" class="hideElement" onclick="swapCompanies(this.parentElement.id, parseInt(this.parentElement.id) + <%=yValue%>, swapInnerContentBottom)">v</button>
					</div>
					<%
						}
					%>
			</div>
		</div>
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
		                <form class="display-inline" action="removecompany" method="post">
	            	    	<input type="hidden" id="removeId" name="companyRemoveId" value="">
	            	    	<input class="btn btn-primary" type="submit" value="Remove" onclick="removeCompany()">
	            	    </form>
		            </div>
		        </div>
		    </div>
		</div>
		<script>
			var id;
	
			function openPopUp(id) {
			    $("#myModal").modal('show');
			    setId(id);
			}
	
			function removeCompany() {
			    $("#myModal").modal('hide');
			    document.getElementById('removeId').value = this.id;
			}
	
			function setId(id) {
			    this.id = id;
			}
			
			$(document).ready(function () {
			    $("#myModal").modal('hide');
			});	
			
			$(document).ready(enableLeftRight());
			$(document).ready(enableTopBottom());
	
			function swapCompanies(divId1, divId2, successCallBack) {
				var idOld = document.getElementById(divId1).getElementsByTagName('input').namedItem('companyId').value;
				var idNew = document.getElementById(divId2).getElementsByTagName('input').namedItem('companyId').value;
				
				$.ajax({
	                type: "POST",
	                url: "swapcompanies",
	                data: {idOld: idOld, idNew: idNew},
	                dataType: "json",
	                success: function(response){
	                	successCallBack(divId1, divId2);
	                },
	                error: function(response, status, error){
	                	console.log(error);
	                }
	            });
			}
			
			function disableButtonsDuringAnimation(bool){
				var btns = $('button');
				for (var i=0; i<btns.length; i++){
					btns[i].disabled = bool;
				}
			}
			
			function swapInnerContentRight(divId1, divId2){
				var divInner1 = document.getElementById(divId1).getElementsByClassName('cellInner')[0];
				var divInner2 = document.getElementById(divId2).getElementsByClassName('cellInner')[0];
				
				$(divInner2).animate({left: "-340px", opacity: 0.8}, 2500);
				$(divInner1).animate({left: "340px", opacity: 0.8}, 2500);
				
				disableButtonsDuringAnimation(true);
				
				setTimeout(function () {
				$(divInner1).css('left', '0px');
				$(divInner2).css('left', '0px');
				
				$(divInner1).css('opacity', '1');
				$(divInner2).css('opacity', '1');
				
				$(divInner1).replaceWith(document.getElementById(divId2).getElementsByClassName('cellInner')[0]);
				$(divInner1).insertBefore(document.getElementById(divId2).getElementsByTagName('button').southBtn);
				
				disableButtonsDuringAnimation(false);
				}, 2600);
			}
			
			function swapInnerContentLeft(divId1, divId2){
				var divInner1 = document.getElementById(divId1).getElementsByClassName('cellInner')[0];
				var divInner2 = document.getElementById(divId2).getElementsByClassName('cellInner')[0];
				
				$(divInner2).animate({left: "340px", opacity: 0.8}, 2500);
				$(divInner1).animate({left: "-340px", opacity: 0.8}, 2500);
				
				disableButtonsDuringAnimation(true);
				
				setTimeout(function () {
				$(divInner1).css('left', '0px');
				$(divInner2).css('left', '0px');
				
				$(divInner1).css('opacity', '1');
				$(divInner2).css('opacity', '1');
				
				$(divInner1).replaceWith(document.getElementById(divId2).getElementsByClassName('cellInner')[0]);
				$(divInner1).insertBefore(document.getElementById(divId2).getElementsByTagName('button').southBtn);
				
				disableButtonsDuringAnimation(false);
				}, 2600);
			}
			
			function swapInnerContentTop(divId1, divId2){
				var divInner1 = document.getElementById(divId1).getElementsByClassName('cellInner')[0];
				var divInner2 = document.getElementById(divId2).getElementsByClassName('cellInner')[0];
				
				$(divInner2).animate({top: "256px", opacity: 0.8}, 2500);
				$(divInner1).animate({top: "-256px", opacity: 0.8}, 2500);
				
				disableButtonsDuringAnimation(true);
				
				setTimeout(function () {
				$(divInner1).css('top', '0px');
				$(divInner2).css('top', '0px');
				
				$(divInner1).css('opacity', '1');
				$(divInner2).css('opacity', '1');
				
				$(divInner1).replaceWith(document.getElementById(divId2).getElementsByClassName('cellInner')[0]);
				$(divInner1).insertBefore(document.getElementById(divId2).getElementsByTagName('button').southBtn);
				
				disableButtonsDuringAnimation(false);
				}, 2600);
			}
			
			function swapInnerContentBottom(divId1, divId2){
				var divInner1 = document.getElementById(divId1).getElementsByClassName('cellInner')[0];
				var divInner2 = document.getElementById(divId2).getElementsByClassName('cellInner')[0];
				
				$(divInner2).animate({top: "-256px", opacity: 0.8}, 2500);
				$(divInner1).animate({top: "256px", opacity: 0.8}, 2500);
				
				disableButtonsDuringAnimation(true);
				
				setTimeout(function () {
				$(divInner1).css('top', '0px');
				$(divInner2).css('top', '0px');
				
				$(divInner1).css('opacity', '1');
				$(divInner2).css('opacity', '1');
				
				$(divInner1).replaceWith(document.getElementById(divId2).getElementsByClassName('cellInner')[0]);
				$(divInner1).insertBefore(document.getElementById(divId2).getElementsByTagName('button').southBtn);
				
				disableButtonsDuringAnimation(false);
				}, 2600);
			}
			
			function enableLeftRight(){
				var rows = document.getElementById("divTable").getElementsByClassName("divRow");
				for (var i = 1; i<rows.length; i++){
					var cells = rows[i].getElementsByClassName("divCell");
					for(var j = 0; j<cells.length; j++) {
						if(j==0 && cells.length > 1){
							var eastButton = cells[j].getElementsByTagName("button").eastBtn;
							eastButton.className = "navigation-button east";
						} else if(j==0 && cells.length == 1) {
						} else if(j==cells.length-1){
							var westButton = cells[j].getElementsByTagName("button").westBtn;
							westButton.className = "navigation-button west";					
						}
						else {
							var eastButton = cells[j].getElementsByTagName("button").eastBtn;
							eastButton.className = "navigation-button east";
							
							var westButton = cells[j].getElementsByTagName("button").westBtn;
							westButton.className = "navigation-button west";		
						}
					}
				}
			}
			
			function enableTopBottom() {
			    var rows = document.getElementById("divTable").getElementsByClassName("divRow");
			    for (var i = 1; i < rows.length; i++) {
			        if (i == 1 && rows.length>3) {
			            var cells = rows[i].getElementsByClassName("divCell");
			            for (var j = 0; j < cells.length; j++) {
			                cells[j].style.paddingTop = "22px";
			                var southButton = cells[j].lastElementChild;
			                southButton.className = "south";
			            }
			        } else if(i == 1 && rows.length==3){
			        	var cells = rows[i].getElementsByClassName("divCell");
			            var cellsNextRow = rows[i + 1].getElementsByClassName("divCell");
			            if (cells.length != cellsNextRow.length) {
			                var difference = cells.length - cellsNextRow.length;
			                for (var j = 0; j < cells.length; j++){
			                	cells[j].style.paddingTop = "22px";
			                }
			                for (var j = 0; j < (cells.length-difference); j++){
			                	var southButton = cells[j].lastElementChild;
				                southButton.className = "south";
			                }
			            } else {
			            	for (var j = 0; j < cells.length; j++) {
			            		cells[j].style.paddingTop = "22px";
			            		var southButton = cells[j].lastElementChild;
			                	southButton.className = "south";
			            	}
			            }
			        } else if (i == 1 && rows.length<3){
			        } else if (i == rows.length - 2) {
			        	var cells = rows[i].getElementsByClassName("divCell");
			            var cellsNextRow = rows[i + 1].getElementsByClassName("divCell");
		                for (var j = 0; j < cells.length; j++) {
		                    var northButton = cells[j].getElementsByTagName("button").northBtn;
		                    northButton.className = "navigation-button north";
		                }
			            if (cells.length != cellsNextRow.length) {
			                var difference = cells.length - cellsNextRow.length;
			                for (var j = 0; j < (cells.length-difference); j++){
			                	var southButton = cells[j].lastElementChild;
				                southButton.className = "south";
			                }
			            } else {
			            	for (var j = 0; j < cells.length; j++) {
			            		var northButton = cells[j].getElementsByTagName("button").northBtn;
				                northButton.className = "navigation-button north";

			                	var southButton = cells[j].lastElementChild;
			                	southButton.className = "south";
			            	}
			            }
			        } else if (i == rows.length - 1) {
			            var cells = rows[i].getElementsByClassName("divCell");
			            for (var j = 0; j < cells.length; j++) {
			                var northButton = cells[j].getElementsByTagName("button").northBtn;
			                northButton.className = "navigation-button north";
			            }
			        } else {
			        	var cells = rows[i].getElementsByClassName("divCell");
				        for (var j = 0; j < cells.length; j++) {
			            	var northButton = cells[j].getElementsByTagName("button").northBtn;
			            	northButton.className = "navigation-button north";

			            	var southButton = cells[j].lastElementChild;
			            	southButton.className = "south";
				        }
			        }
			    }
			}
		</script>
	</body>
</html>