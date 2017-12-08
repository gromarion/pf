var globalGradeVisible = false;
var endpointAvailabilityVisible = false;
var totalResourcesVisible = false;
var averageQualityVisible = false;
var incorrectDataVisible = false;
var incompleteDataVisible = false;
var semanticallyIncorrectVisible = false;
var externalLinkVisible = false;

var globalGradeValue = 0;
var globalGradeLetterParam = "";
var endpointAvailabilityValue = 0;
var totalResourcesValue = 0;
var averageQualityValue = 0;
var incorrectDataValue = 0;
var incompleteDataValue = 0;
var semanticallyIncorrectValue = 0;
var externalLinkValue = 0;

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
		endpointAvailabilityParam,
		totalResourcesParam,
		averageQualityParam,
		incorrectDataParam,
		incompleteDataParam,
		semanticallyIncorrectParam,
		externalLinkParam) {
	globalGradeValue = globalGradeParam;
	globalGradeLetterValue = globalGradeLetterParam;
	endpointAvailabilityValue = endpointAvailabilityParam;
	totalResourcesValue = totalResourcesParam;
	averageQualityValue = averageQualityParam;
	incorrectDataValue = incorrectDataParam;
	incompleteDataValue = incompleteDataParam;
	semanticallyIncorrectValue = semanticallyIncorrectParam;
	externalLinkValue = externalLinkParam;

	var globalGradeVisible = false;
	var endpointAvailabilityVisible = false;
	var totalResourcesVisible = false;
	var averageQualityVisible = false;
	var incorrectDataVisible = false;
	var incompleteDataVisible = false;
	var semanticallyIncorrectVisible = false;
	var externalLinkVisible = false;

	startAnimation();
}

function startAnimation() {
	startGlobalGradeAnimation();

	if ($('#endpoint-availability').visible(true) && !endpointAvailabilityVisible) {
		endpointAvailabilityVisible = true;
	  animate('endpoint-availability', 0.0, endpointAvailabilityValue);
	}
	if ($('#total-resources').visible(true) && !totalResourcesVisible) {
		totalResourcesVisible = true;
	  animate('total-resources', 0, totalResourcesValue);
	}
	if ($('#average-quality').visible(true) && !averageQualityVisible) {
		averageQualityVisible = true;
	  animate('average-quality', 0, averageQualityValue);
	}
	if ($('#incorrect-data').visible(true) && !incorrectDataVisible) {
		incorrectDataVisible = true;
	  animate('incorrect-data', 0, incorrectDataValue);
	}
	if ($('#incomplete-data').visible(true) && !incompleteDataVisible) {
		incompleteDataVisible = true;
	  animate('incomplete-data', 0, incompleteDataValue);
	}
	if ($('#semantically-incorrect').visible(true) && !semanticallyIncorrectVisible) {
		semanticallyIncorrectVisible = true;
	  animate('semantically-incorrect', 0, semanticallyIncorrectValue);
	}
	if ($('#external-link').visible(true) && !externalLinkVisible) {
		externalLinkVisible = true;
	  animate('external-link', 0, externalLinkValue);
	}
}

function startGlobalGradeAnimation() {
	let componentId = globalGradeLetterValue.toLowerCase() + '-global-grade';

	if ($('#' + componentId).visible(true) && !globalGradeVisible) {
		globalGradeVisible = true;
	  animate(componentId, 0.0, globalGradeValue);
	}
}

$( window ).scroll(function() {
	console.log("scroll");
	startAnimation();
});
