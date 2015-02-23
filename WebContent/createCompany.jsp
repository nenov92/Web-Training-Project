<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Create Company</title>
		
		<link rel="stylesheet" type="text/css" href="css/app.css">
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<%@include file="navigation.jsp" %>
		<br />
		<h1 class="container-header">Fill in Company Details</h1>
		<div class="container">
			<form action="create" method="post" enctype="multipart/form-data" onsubmit="return formIsValidForSaving()">
			    <div class="table-row">
			        <div class="col-left" id="name">Name:</div>
			        <div class="col-right" id="companyNameInput">
			        	<input type="text" name="companyName" id="companyName" maxlength="50" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="address">Address:</div>
			        <div class="col-right">
			            <input type="text" name="companyAddress" id="companyAddress" maxlength="100" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="establishedDate">Established Date:</div>
			        <div class="col-right">
			            <input type="date" name="companyEstablishedDate" id="companyEstablishedDate"/>
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="bulstat">Bulstat:</div>
			        <div class="col-right" id="companyBulstatInput">
			            <input type="text" name="companyBulstat" id="companyBulstat" maxlength="50" />
			        </div>
			    </div>
			    <div class="table-row">
			        <div class="col-left" id="logo">Logo:</div>
			        <div class="col-right">
			            <input type="file" name="companyLogo" id="companyLogo" onchange="validateLogo()" />
			        </div>
	    		</div>
	    		<div class="table-row">
			        <div class="col-left">Company Boss:</div>
			        <div class="col-right">
			            <select name="companyBoss" disabled></select>
			        </div>
	    		</div>
				<div class="table-row">
					<div class="col-left">
						<input type="submit" value="Create Company" id="createCompany"/>
				   	</div>
				    <div class="col-right">
				    	<input type="button" value="Back" onclick="window.location.replace(document.referrer)" />
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
		                <p>All company details must be filled in!</p>
		                <span style="font-size: small;">* Name and Bulstat should be unique!</span>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">Okay</button>
		            </div>
		        </div>
		    </div>
		</div>
		<div class="loadingIndicator"></div>
		<script>
			var nameIsValid = false,
				addressIsValid = false,
				establishedDateIsValid = false,
				bulstatIsValid = false,
				logoIsValid = false;
			
			function openPopUp() {
			    $("#myModal").modal('show');
			}
			
			$(document).ready(function () {
			    $("#myModal").modal('hide');
			});	
			
			$body = $("body");

			$(document).on({
			    ajaxStart: function() {
			    	$body.addClass("loading");
			    },
			    ajaxStop: function() {
			    	$body.removeClass("loading");
			    }    
			});
			
			$("#companyName").blur(function() {
				if($('#companyName').val() == "" || $('#companyName').val() == null){
					if(!($("#name").html().indexOf("*")>=0)){
						$("#name").append("<span id=\"nameSpan\">*</span>");
						$("#name").css("color", "red");
					}
					if($("#companyNameInput").has("img").length>0) {
                		$("#companyNameInput").children("#indicator").remove();
                	}
					nameIsValid = false;
				} else {
					var name = $('#companyName').val();
					
					$("#name").children("#nameSpan").remove();
					$("#name").css("color", "#000");
					
					$.ajax({
		                type: "POST",
		                url: "validateform",
		                data: {companyName: name},
		                dataType: "json",
		                success: function(response){
		                	if($("#companyNameInput").has("img").length>0) {
		                		$("#companyNameInput").children("#indicator").remove();
		                		$("#companyNameInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
		                	} else {
			                	$("#companyNameInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
		                	}
		                	nameIsValid = true;
		                },
		                error: function(response, status, error){
		                	if($("#companyNameInput").has("img").length>0) {
		                		$("#companyNameInput").children("#indicator").remove();
		                		$("#companyNameInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
		                	} else {
			                	$("#companyNameInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
		                	}
		                	nameIsValid = false;
		                }
		            });
				}
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
			
			$("#companyBulstat").blur(function() {
				if($('#companyBulstat').val() == "" || $('#companyBulstat').val() == null){
					if(!($("#bulstat").html().indexOf("*")>=0)){
						$("#bulstat").append("<span id=\"bulstatSpan\">*</span>");
						$("#bulstat").css("color", "red");
					}
					bulstatIsValid = false;
				} else {
					var bulstat = $('#companyBulstat').val();
					
					$("#bulstat").children("#bulstatSpan").remove();
					$("#bulstat").css("color", "#000");
					
					$.ajax({
		                type: "POST",
		                url: "validateform",
		                data: {companyBulstat: bulstat},
		                dataType: "json",
		                success: function(response){
		                	if($("#companyBulstatInput").has("img").length>0) {
		                		$("#companyBulstatInput").children("#indicator").remove();
		                		$("#companyBulstatInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
		                	} else {
			                	$("#companyBulstatInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
		                	}
		                	bulstatIsValid = true;
		                },
		                error: function(response, status, error){
		                	if($("#companyBulstatInput").has("img").length>0) {
		                		$("#companyBulstatInput").children("#indicator").remove();
		                		$("#companyBulstatInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
		                	} else {
			                	$("#companyBulstatInput").append("<img id=\"indicator\" src=\"/MyWebProjectStaticContent/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
		                	}
		                	bulstatIsValid = false;
		                }
		            });
				}
			});
			
			function validateLogo() {
				if($('#companyLogo').val() == "" || $('#companyLogo').val() == null){
					if(!($("#logo").html().indexOf("*")>=0)){
						$("#logo").append("<span id=\"logoSpan\">*</span>");
						$("#logo").css("color", "red");
					}
					logoIsValid = false;
				} else {
					$("#logo").children("#logoSpan").remove();
					$("#logo").css("color", "#000");
					logoIsValid = true;
				}
			};
			
			function formIsValidForSaving() {
				if(nameIsValid && addressIsValid && establishedDateIsValid && bulstatIsValid && logoIsValid){
					return true;
				}
				openPopUp();
				return false;
			};
		</script>
	</body>
</html>