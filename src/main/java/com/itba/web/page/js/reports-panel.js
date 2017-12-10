var booleans = [
	[false, false], //0: globalGradeVisible 1 & 2
	[false, false], //1: endpointAvailabilityVisible 1 & 2
	[false, false], //2: endpointQualityVisible 1 & 2
	[false, false], //3: totalResourcesVisible 1 & 2
	[false, false], //4: averageQualityVisible 1 & 2
	[false, false], //5: incorrectDataVisible 1 & 2
	[false, false], //6: incompleteDataVisible 1 & 2
	[false, false], //7: semanticallyIncorrectVisible 1 & 2
	[false, false]];//8: externalLinkVisible 1 & 2

var values = [
	[0, 0],  //0: globalGradeValue 1 & 2
	["", ""],//1: globalGradeLetterValue 1 & 2
	[0, 0],  //2: endpointQualityValue 1 & 2
	[0, 0],  //3: endpointAvailabilityValue 1 & 2
	[0, 0],  //4: totalResourcesValue 1 & 2
	[0, 0],  //5: averageQualityValue 1 & 2
	[0, 0],  //6: incorrectDataValue 1 & 2
	[0, 0],  //7: incompleteDataValue 1 & 2
	[0, 0],  //8: semanticallyIncorrectValue 1 & 2
	[0, 0]]; //9: externalLinkValue 1 & 2

var panelNumberValue = 0;

function animate(id, start, end) {
	var numAnim = new CountUp(id, start, end);

	if (!numAnim.error) {
	  numAnim.start();
	} else {
	  console.error(numAnim.error);
	}
}

function initializeReportsPanel(
		globalGradeParam,
		globalGradeLetterParam,
		endpointQualityParam,
		endpointAvailabilityParam,
		totalResourcesParam,
		averageQualityParam,
		incorrectDataParam,
		incompleteDataParam,
		semanticallyIncorrectParam,
		externalLinkParam,
		panelNumberParam) {
	values[panelNumberParam - 1][0] = globalGradeParam;
	values[panelNumberParam - 1][1] = globalGradeLetterParam;
	values[panelNumberParam - 1][2] = endpointQualityParam;
	values[panelNumberParam - 1][3] = endpointAvailabilityParam;
	values[panelNumberParam - 1][4] = totalResourcesParam;
	values[panelNumberParam - 1][5] = averageQualityParam;
	values[panelNumberParam - 1][6] = incorrectDataParam;
	values[panelNumberParam - 1][7] = incompleteDataParam;
	values[panelNumberParam - 1][8] = semanticallyIncorrectParam;
	values[panelNumberParam - 1][9] = externalLinkParam;
	panelNumberValue = panelNumberParam;

	booleans[panelNumberParam - 1][0] = false;
	booleans[panelNumberParam - 1][1] = false;
	booleans[panelNumberParam - 1][3] = false;
	booleans[panelNumberParam - 1][4] = false;
	booleans[panelNumberParam - 1][5] = false;
	booleans[panelNumberParam - 1][6] = false;
	booleans[panelNumberParam - 1][7] = false;
	booleans[panelNumberParam - 1][8] = false;

	startAnimation();
}

function startAnimation() {
	startGlobalGradeAnimation();

	if ($('#endpoint-quality-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][2]) {
		animateAllPanels('endpoint-quality-', 2, 2);
	}
	if ($('#endpoint-availability-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][1]) {
		animateAllPanels('endpoint-availability-', 1, 3);
	}
	if ($('#total-resources-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][3]) {
		animateAllPanels('total-resources-', 3, 4);
	}
	if ($('#average-quality-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][4]) {
		animateAllPanels('average-quality-', 4, 5);
	}
	if ($('#incorrect-data-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][5]) {
		animateAllPanels('incorrect-data-', 5, 6);
	}
	if ($('#incomplete-data-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][6]) {
		animateAllPanels('incomplete-data-', 6, 7);
	}
	if ($('#semantically-incorrect-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][7]) {
		animateAllPanels('semantically-incorrect-', 7, 8);
	}
	if ($('#external-link-' + panelNumberValue).visible(true) && !booleans[panelNumberValue - 1][8]) {
		animateAllPanels('external-link-', 8, 9);
	}
}

function startGlobalGradeAnimation() {
	let componentId = values[panelNumberValue - 1][1].toLowerCase() + '-global-grade-' + panelNumberValue;

	if ($('#' + componentId).visible(true) && !booleans[panelNumberValue - 1][0]) {
		booleans[panelNumberValue - 1][0] = true;
	  animate(componentId, 0, values[panelNumberValue - 1][0]);
	}
}

function animateAllPanels(id, booleanIndex, valueIndex) {
	var i = 0;
	var limit = 1;
	if (panelNumberValue > 1) {
		limit = panelNumberValue;
	}

	for (i = 0; i < limit; i++) {
		booleans[i][booleanIndex] = true;

	  animate(id + (i + 1), 0, values[i][valueIndex]);
	}
}

window.onload = function() {
	startAnimation();

	$(window).scroll(function() {
		console.log("scroll");
		startAnimation();
	});
};
