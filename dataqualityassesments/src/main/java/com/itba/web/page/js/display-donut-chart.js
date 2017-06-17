function displayChart(chartID) {
	$(chartID).show();
	animate();
	$("#hideChart").show();
	$("#displayChart").hide();
}

function hideChart(chartID) {
	$("#displayChart").show();
	$(chartID).hide();
	$("#hideChart").hide();
}
