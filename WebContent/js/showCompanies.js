var companyId,
	x = null,
	y = null;

function setX(x) {
	if (x != null) {
		$("#x-axis").val(x);
	}
}

function setY(y) {
	if (y != null) {
		$("#y-axis").val(y);
	}
}

function openRemovePopUp(companyId) {
	$("#myModal").modal('show');
	setCompanyId(companyId);

	// Another way for popup
	//if(window.confirm("Are you sure?")){
	//	setCompanyId(companyId);
	//	removeCompany();
	//	$("#removeForm").submit();
	//}
}

function removeCompany() {
	$("#myModal").modal('hide');
	$("#removeId").val(this.companyId);
}

function setCompanyId(companyId) {
	this.companyId = companyId;
}

$(document).ready(function() {
	// if there is user notification hide it after given time
	if ($("#userNotification")) {
		$("#userNotification").fadeOut(2500);
	}

	// show swapping buttons
	enableLeftRight();
	enableTopBottom();
});

function swapCompanies(divId1, divId2, successCallBack) {
	var idOld = $("#" + divId1).children('#cellInner').children('.cellContent').children('#companyId').val();
	var idNew = $("#" + divId2).children('#cellInner').children('.cellContent').children('#companyId').val();

	$.ajax({
		type: "POST",
		url: "swapcompanies",
		data: {
			idOld: idOld,
			idNew: idNew
		},
		dataType: "json",
		success: function(response) {
			successCallBack(divId1, divId2);
		},
		error: function(response, status, error) {}
	});
}

function disableButtonsDuringAnimation(bool) {
	var swappingBtns = $('button');
	for (var i = 0; i < swappingBtns.length; i++) {
		swappingBtns[i].disabled = bool;
	}

	var navigationBtns = $('.submit-button');
	for (var i = 0; i < navigationBtns.length; i++) {
		navigationBtns[i].disabled = bool;
	}
}

function swapInnerContentRight(divId1, divId2) {
	var divInner1 = $("#" + divId1).children('.cellInner').get(0);
	var divInner2 = $("#" + divId2).children('.cellInner').get(0);

	$(divInner2).animate({
		left: "-340px",
		opacity: 0.8
	}, 2500);
	$(divInner1).animate({
		left: "340px",
		opacity: 0.8
	}, 2500);

	disableButtonsDuringAnimation(true);

	setTimeout(function() {
		$(divInner1).css('left', '0px');
		$(divInner2).css('left', '0px');

		$(divInner1).css('opacity', '1');
		$(divInner2).css('opacity', '1');

		$(divInner1).replaceWith($("#" + divId2).children('.cellInner').get(0));
		$(divInner1).insertBefore($("#" + divId2).children("#southBtn"));

		disableButtonsDuringAnimation(false);
	}, 2600);
}

function swapInnerContentLeft(divId1, divId2) {
	var divInner1 = $("#" + divId1).children('.cellInner').get(0);
	var divInner2 = $("#" + divId2).children('.cellInner').get(0);

	$(divInner2).animate({
		left: "340px",
		opacity: 0.8
	}, 2500);
	$(divInner1).animate({
		left: "-340px",
		opacity: 0.8
	}, 2500);

	disableButtonsDuringAnimation(true);

	setTimeout(function() {
		$(divInner1).css('left', '0px');
		$(divInner2).css('left', '0px');

		$(divInner1).css('opacity', '1');
		$(divInner2).css('opacity', '1');

		$(divInner1).replaceWith($("#" + divId2).children('.cellInner').get(0));
		$(divInner1).insertBefore($("#" + divId2).children("#southBtn"));

		disableButtonsDuringAnimation(false);
	}, 2600);
}

function swapInnerContentTop(divId1, divId2) {
	var divInner1 = $("#" + divId1).children('.cellInner').get(0);
	var divInner2 = $("#" + divId2).children('.cellInner').get(0);

	$(divInner2).animate({
		top: "256px",
		opacity: 0.8
	}, 2500);
	$(divInner1).animate({
		top: "-256px",
		opacity: 0.8
	}, 2500);

	disableButtonsDuringAnimation(true);

	setTimeout(function() {
		$(divInner1).css('top', '0px');
		$(divInner2).css('top', '0px');

		$(divInner1).css('opacity', '1');
		$(divInner2).css('opacity', '1');

		$(divInner1).replaceWith($("#" + divId2).children('.cellInner').get(0));
		$(divInner1).insertBefore($("#" + divId2).children("#southBtn"));

		disableButtonsDuringAnimation(false);
	}, 2600);
}

function swapInnerContentBottom(divId1, divId2) {
	var divInner1 = $("#" + divId1).children('.cellInner').get(0);
	var divInner2 = $("#" + divId2).children('.cellInner').get(0);

	$(divInner2).animate({
		top: "-256px",
		opacity: 0.8
	}, 2500);
	$(divInner1).animate({
		top: "256px",
		opacity: 0.8
	}, 2500);

	disableButtonsDuringAnimation(true);

	setTimeout(function() {
		$(divInner1).css('top', '0px');
		$(divInner2).css('top', '0px');

		$(divInner1).css('opacity', '1');
		$(divInner2).css('opacity', '1');

		$(divInner1).replaceWith($("#" + divId2).children('.cellInner').get(0));
		$(divInner1).insertBefore($("#" + divId2).children("#southBtn"));

		disableButtonsDuringAnimation(false);
	}, 2600);
}

function enableLeftRight() {
	var rows = document.getElementById("divTable").getElementsByClassName("divRow");
	for (var i = 0; i < rows.length; i++) {
		var cells = rows[i].getElementsByClassName("divCell");
		for (var j = 0; j < cells.length; j++) {
			if (j == 0 && cells.length > 1) {
				// First cell on this row
				var eastButton = cells[j].getElementsByTagName("button").eastBtn;
				eastButton.className = "navigation-button east";
			} else if (j == 0 && cells.length == 1) {
				// There is only one cell per row, so east/west buttons are not needed
			} else if (j == cells.length - 1) {
				// Last cell on this row
				var westButton = cells[j].getElementsByTagName("button").westBtn;
				westButton.className = "navigation-button west";
			} else {
				// Every cell between first and last
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
	for (var i = 0; i < rows.length; i++) {
		if (i == 0 && rows.length > 2) {
			var cells = rows[i].getElementsByClassName("divCell");
			for (var j = 0; j < cells.length; j++) {
				cells[j].style.paddingTop = "22px";
				var southButton = cells[j].lastElementChild;
				southButton.className = "south";
			}
		} else if (i == 0 && rows.length == 2) {
			var cells = rows[i].getElementsByClassName("divCell");
			var cellsNextRow = rows[i + 1].getElementsByClassName("divCell");
			if (cells.length != cellsNextRow.length) {
				var difference = cells.length - cellsNextRow.length;
				for (var j = 0; j < cells.length; j++) {
					cells[j].style.paddingTop = "22px";
				}
				for (var j = 0; j < (cells.length - difference); j++) {
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
		} else if (i == 0 && rows.length < 2) {
			// There is only one row, so north/south buttons are not needed only padding
			var cells = rows[i].getElementsByClassName("divCell");
			for (var j = 0; j < cells.length; j++) {
				cells[j].style.paddingTop = "22px";
			}
		} else if (i == rows.length - 2) {
			var cells = rows[i].getElementsByClassName("divCell");
			var cellsNextRow = rows[i + 1].getElementsByClassName("divCell");
			for (var j = 0; j < cells.length; j++) {
				var northButton = cells[j].getElementsByTagName("button").northBtn;
				northButton.className = "navigation-button north";
			}
			if (cells.length != cellsNextRow.length) {
				var difference = cells.length - cellsNextRow.length;
				for (var j = 0; j < (cells.length - difference); j++) {
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