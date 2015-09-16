<?php
header("Content-type: text/plain");

if (!isset($_POST["width"]) || !isset($_POST["width"])) {
	header("HTTP/1.1 400 Invalid Request");
	die("Width or height is missing from POST request.");
}

$width = $_POST["width"];
$height = $_POST["height"];

// the "-d64 -Xmx512M" was needed to fix a "Could not reserve enough space for object heap" error
$output = shell_exec("java -d64 -Xmx512M MazeBuilder " . $width . " " . $height);
if (is_null($output)) {
	$output = "Sorry, an error occured: null output returned from shell_exec";
}
echo $output;


?>