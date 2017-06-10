function displayChart() {
	console.log("display chart");

	$("#chart").show();
	animate();
	$("#hideChart").show();
	$("#displayChart").hide();
}

function hideChart() {
	console.log("hide chart");

	$("#displayChart").show();
	$("#chart").hide();
	$("#hideChart").hide();
}
