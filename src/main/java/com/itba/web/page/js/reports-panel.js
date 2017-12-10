var globalGradeVisible = false;
var endpointAvailabilityVisible = false;
var endpointQualityVisible = false;
var totalResourcesVisible = false;
var averageQualityVisible = false;
var incorrectDataVisible = false;
var incompleteDataVisible = false;
var semanticallyIncorrectVisible = false;
var externalLinkVisible = false;

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

	startAnimation();
}

function startAnimation() {
	startGlobalGradeAnimation();

	if ($('#endpoint-quality-1').visible() && !endpointQualityVisible) {
		endpointQualityVisible = true;
		animateAllPanels('endpoint-quality-', 2);
	}
	if ($('#endpoint-availability-1').visible() && !endpointAvailabilityVisible) {
		endpointAvailabilityVisible = true;
		animateAllPanels('endpoint-availability-', 3);
	}
	if ($('#total-resources-1').visible() && !totalResourcesVisible) {
		totalResourcesVisible = true;
		animateAllPanels('total-resources-', 4);
	}
	if ($('#average-quality-1').visible() && !averageQualityVisible) {
		averageQualityVisible = true;
		animateAllPanels('average-quality-', 5);
	}
	if ($('#incorrect-data-1').visible() && !incorrectDataVisible) {
		incorrectDataVisible = true;
		animateAllPanels('incorrect-data-', 6);
	}
	if ($('#incomplete-data-1').visible() && !incompleteDataVisible) {
		incompleteDataVisible = true;
		animateAllPanels('incomplete-data-', 7);
	}
	if ($('#semantically-incorrect-1').visible() && !semanticallyIncorrectVisible) {
		semanticallyIncorrectVisible = true;
		animateAllPanels('semantically-incorrect-', 8);
	}
	if ($('#external-link-1').visible() && !externalLinkVisible) {
		externalLinkVisible = true;
		animateAllPanels('external-link-', 9);
	}
}

function startGlobalGradeAnimation() {
	let componentId = values[panelNumberValue - 1][1].toLowerCase() + '-global-grade-1';
	let i = 0;
	let limit = 1;

	if (panelNumberValue > 1) {
		limit = panelNumberValue;
	}
	if ($('#' + componentId).visible() && !booleans[0][0]) {
		for (i = 0; i < limit; i++) {
			booleans[0][i] = true;
		  animate(componentId, 0, values[0][i]);
		}
	}
}

function animateAllPanels(id, valueIndex) {
	let i = 0;
	let limit = 1;

	if (panelNumberValue > 1) {
		limit = panelNumberValue;
	}

	for (i = 0; i < limit; i++) {
	  animate(id + (i + 1), 0, values[valueIndex][i]);
	}
}

window.onload = function() {
	startAnimation();

	$(window).scroll(function() {
		startAnimation();
	});
};
