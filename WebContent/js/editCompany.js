var addressIsValid = true,
	establishedDateIsValid = true;


function openPopUp() {
	$("#myModal").modal('show');
}

$(document).ready(function() {
	$("#myModal").modal('hide');
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

function formIsValidForSaving() {
	if (addressIsValid && establishedDateIsValid) {
		return true;
	}
	openPopUp();
	return false;
};