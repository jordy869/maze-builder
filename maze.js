// variables for the min/max values of the maze width/height
var MIN_WIDTH = 3;
var MIN_HEIGHT = 3;
var MAX_WIDTH = 52;
var MAX_HEIGHT = 33;

// called when the page is done loading
$(document).ready(function() {
	// dynamically set these values in the HTML so I don't have to 
	// change the maze page everytime I change one of these values
	$("#min-width").text(MIN_WIDTH);
	$("#min-height").text(MIN_HEIGHT);
	$("#max-width").text(MAX_WIDTH);
	$("#max-height").text(MAX_HEIGHT);

	// hide certain resources that will be displayed later as Maze Builder is used
	$("#loading").hide();
	$("#width img").hide();
	$("#height img").hide();
	$("#width span").hide();
	$("#height span").hide();

	// hook up buttons to their behavior
	$("#build-button").on("click", buildMaze);
	$("#print-button").on("click", function() {window.print();});
});

// Builds the maze based on the width and height parameters; if something
// is wrong with the input data, an error message will be displayed
function buildMaze(event) {
	var width = parseInt($("#width input").val());
	var height = parseInt($("#height input").val());

	var successWidth = validateField("width", width, MIN_WIDTH, MAX_WIDTH);
	var successHeight = validateField("height", height, MIN_HEIGHT, MAX_HEIGHT);

	if (successWidth && successHeight) {
		$("#loading").show();
		
		$("textarea").load(
			"mazebuilder.php",                  // location
			{"width": width, "height": height}, // params
			function(response, status, xhr) {   // function to execute when loading is complete
				$("#loading").hide();
				determineFontSize(width, height);
				if (status == "error") {
					$("textarea").text("Sorry, an internal error has occured: " + response);
				}
			});
	} else {
		$("textarea").text("Way to go. You broke it.");
	}
	
}

// Validates the input field (width or height) and displays
// a descriptive error message if something is incorrect
function validateField(fieldName, value, minParam, maxParam) {
	$("#" + fieldName + " img").show();
	$("#" + fieldName + " span").show();
	if (isNaN(value)) {
		$("#" + fieldName + " span").text("That's not a number dude.");
	} else if (value < minParam || value > maxParam) {
		$("#" + fieldName + " span").text(
				"Please make sure the " + fieldName + 
				" is between " + minParam + " and " + maxParam);
	} else {
		$("#" + fieldName + " input").val(value);
		$("#" + fieldName + " img").hide();
		$("#" + fieldName + " span").hide();
		return true;
	}
	return false;
}

// Determines the font size to use and how many rows/columns the textarea
// needs as the requested width and height of the maze change 
function determineFontSize(width, height) {
	if (width < 33 && height < 16) {
		$("textarea").css({'fontSize': 18});
		$("textarea").attr("rows", 45);
		$("textarea").attr("cols", 76);
	} else if (width < 44 && height < 29) {
		$("textarea").css({'fontSize': 14});
		$("textarea").attr("rows", 60);
		$("textarea").attr("cols", 104);
	} else if (width < 53 && height < 34) {
		$("textarea").css({'fontSize': 12});
		$("textarea").attr("rows", 68);
		$("textarea").attr("cols", 120);
	}
}
