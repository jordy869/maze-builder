var MIN_WIDTH = 3;
var MIN_HEIGHT = 3;
var MAX_WIDTH = 120;
var MAX_HEIGHT = 33;

$(document).ready(function() {
	//$("#min-width").text(MIN_WIDTH);
	$("#min-height").text(MIN_HEIGHT);
	$("#max-width").text(MAX_WIDTH);
	$("#max-height").text(MAX_HEIGHT);

	$("#build-button").on("click", buildMaze);
	$("#loading").hide();
	$("img#X-width").hide();
	$("img#X-height").hide();

	$("#maze-content").attr("rows", 34);
	$("#maze-content").attr("cols", 60);
});

function buildMaze(event) {
	var width = parseInt($("#width-value").val());
	var height = parseInt($("#height-value").val());

	var successWidth = validateField("width", width, MIN_WIDTH, MAX_WIDTH);
	var successHeight = validateField("height", height, MIN_HEIGHT, MAX_HEIGHT);

	if (successWidth && successHeight) {
		$("#loading").show();
		
		$("#maze-content").load(
			"mazebuilder.php", // location
			{"width": width, "height": height}, // params
			function(response, status, xhr) { // function
				$("#loading").hide();
				if (status == "error") {
					$("#maze-content").text("Sorry, an internal error has occured: " + response);
				}
			});
		determineFontSize(width, height);
		
	} else {
		$("#maze-content").text("Way to go. You broke it.");
	}
	
}

function validateField(fieldName, value, minParam, maxParam) {
	$("img#X-" + fieldName).show();
	$("#" + fieldName + "-error-message").show();
	if (isNaN(value)) {
		$("#" + fieldName + "-error-message").text("That's not a number dude.");
	} else if (value < minParam || value > maxParam) {
		$("#" + fieldName + "-error-message").text("Please make sure the " + fieldName + " is between " + minParam + " and " + maxParam);
	} else {
		$("#" + fieldName + "-value").val(value);
		$("img#X-" + fieldName).hide();
		$("#" + fieldName + "-error-message").hide();
		return true;
	}
	return false;
}

function determineFontSize(width, height) {
	if (width < 33 && height < 16) {
		$("#maze-content").css({'fontSize': 18});
		$("#maze-content").attr("rows", 45);
		$("#maze-content").attr("cols", 76);
	} else if (width < 44 && height < 29) {
		$("#maze-content").css({'fontSize': 14});
		$("#maze-content").attr("rows", 60);
		$("#maze-content").attr("cols", 104);
	} else if (width < 53 && height < 34) {
		$("#maze-content").css({'fontSize': 12});
		$("#maze-content").attr("rows", 68);
		$("#maze-content").attr("cols", 120);
	}
}