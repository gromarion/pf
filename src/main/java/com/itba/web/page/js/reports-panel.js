var globalGradeVisible = false;
var endpointAvailabilityVisible = false;
var totalResourcesVisible = false;
var averageQualityVisible = false;
var incorrectDataVisible = false;
var incompleteDataVisible = false;
var semanticallyIncorrectVisible = false;
var externalLinkVisible = false;

var globalGradeValue = 0;
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

function initialize(
		globalGradeParam,
		endpointAvailabilityParam,
		totalResourcesParam,
		averageQualityParam,
		incorrectDataParam,
		incompleteDataParam,
		semanticallyIncorrectParam,
		externalLinkParam) {
	globalGradeValue = globalGradeParam;
	endpointAvailabilityValue = endpointAvailabilityParam;
	totalResourcesValue = totalResourcesParam;
	averageQualityValue = averageQualityParam;
	incorrectDataValue = incorrectDataParam;
	incompleteDataValue = incompleteDataParam;
	semanticallyIncorrectValue = semanticallyIncorrectParam;
	externalLinkValue = externalLinkParam;

	startAnimation();
}

function startAnimation() {
	if ($('#global-grade').visible(true) && !globalGradeVisible) {
		globalGradeVisible = true;
	  animate('global-grade', 0.0, 93.2);
	}
	if ($('#endpoint-availability').visible(true) && !endpointAvailabilityVisible) {
		endpointAvailabilityVisible = true;
	  animate('endpoint-availability', 0.0, 89.1);
	}
	if ($('#total-resources').visible(true) && !totalResourcesVisible) {
		totalResourcesVisible = true;
	  animate('total-resources', 0, 246);
	}
	if ($('#average-quality').visible(true) && !averageQualityVisible) {
		averageQualityVisible = true;
	  animate('average-quality', 0, 81.27);
	}
	if ($('#incorrect-data').visible(true) && !incorrectDataVisible) {
		incorrectDataVisible = true;
	  animate('incorrect-data', 0, 52.17);
	}
	if ($('#incomplete-data').visible(true) && !incompleteDataVisible) {
		incompleteDataVisible = true;
	  animate('incomplete-data', 0, 26.09);
	}
	if ($('#semantically-incorrect').visible(true) && !semanticallyIncorrectVisible) {
		semanticallyIncorrectVisible = true;
	  animate('semantically-incorrect', 0, 13.04);
	}
	if ($('#external-link').visible(true) && !externalLinkVisible) {
		externalLinkVisible = true;
	  animate('external-link', 0, 8.7);
	}
}

$( window ).scroll(function() {
	console.log("scroll");
	startAnimation();
});
