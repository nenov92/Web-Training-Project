var nameIsValid = false,
	addressIsValid = false,
	establishedDateIsValid = false,
	bulstatIsValid = false,
	logoIsValid = false;

function openPopUp() {
	$("#myModal").modal('show');
}

$(document).ready(function() {
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
	if ($('#companyName').val() == "" || $('#companyName').val() == null) {
		if (!($("#name").html().indexOf("*") >= 0)) {
			$("#name").append("<span id=\"nameSpan\">*</span>");
			$("#name").css("color", "red");
		}
		if ($("#companyNameInput").has("img").length > 0) {
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
			data: {
				companyName: name
			},
			dataType: "json",
			success: function(response) {
				if ($("#companyNameInput").has("img").length > 0) {
					$("#companyNameInput").children("#indicator").remove();
					$("#companyNameInput").append("<img id=\"indicator\" src=\"css/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
				} else {
					$("#companyNameInput").append("<img id=\"indicator\" src=\"css/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
				}
				nameIsValid = true;
			},
			error: function(response, status, error) {
				if ($("#companyNameInput").has("img").length > 0) {
					console.log("There is image");
					$("#companyNameInput").children("#indicator").remove();
					$("#companyNameInput").append("<img id=\"indicator\" src=\"css/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
				} else {
					$("#companyNameInput").append("<img id=\"indicator\" src=\"css/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
				}
				nameIsValid = false;
			}
		});
	}
});

$("#companyAddress").blur(function() {
	if ($('#companyAddress').val() == "" || $('#companyAddress').val() == null) {
		if (!($("#address").html().indexOf("*") >= 0)) {
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
	if ($('#companyEstablishedDate').val() == "" || $('#companyEstablishedDate').val() == null) {
		if (!($("#establishedDate").html().indexOf("*") >= 0)) {
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
	if ($('#companyBulstat').val() == "" || $('#companyBulstat').val() == null) {
		if (!($("#bulstat").html().indexOf("*") >= 0)) {
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
			data: {
				companyBulstat: bulstat
			},
			dataType: "json",
			success: function(response) {
				if ($("#companyBulstatInput").has("img").length > 0) {
					$("#companyBulstatInput").children("#indicator").remove();
					$("#companyBulstatInput").append("<img id=\"indicator\" src=\"css/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
				} else {
					$("#companyBulstatInput").append("<img id=\"indicator\" src=\"css/images/tick.png\" height=\"20\" width=\"20\" alt=\"tick\">");
				}
				bulstatIsValid = true;
			},
			error: function(response, status, error) {
				if ($("#companyBulstatInput").has("img").length > 0) {
					$("#companyBulstatInput").children("#indicator").remove();
					$("#companyBulstatInput").append("<img id=\"indicator\" src=\"css/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
				} else {
					$("#companyBulstatInput").append("<img id=\"indicator\" src=\"css/images/cross.png\" height=\"20\" width=\"20\" alt=\"cross\">");
				}
				bulstatIsValid = false;
			}
		});
	}
});

function validateLogo() {
	if ($('#companyLogo').val() == "" || $('#companyLogo').val() == null) {
		if (!($("#logo").html().indexOf("*") >= 0)) {
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
	if (nameIsValid && addressIsValid && establishedDateIsValid && bulstatIsValid && logoIsValid) {
		return true;
	}
	openPopUp();
	return false;
};