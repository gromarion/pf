var numAnim = new CountUp("global-grade", 0.0, 93.0);

if (!numAnim.error) {
  numAnim.start();
} else {
  console.error(numAnim.error);
}
