var data = {
  "data": [{
    "Success": 3412,
    "Pending": 2107
  }]
}

var newData = [];
for (var key in data.data[0]) {
 // console.log(key)
  var thisData = {
    "Name": key,
    "Value": data.data[0][key]
  }
  newData.push(thisData)
}

var width = 360;
var height = 360;
var outradius = Math.min(width, height) / 2;
var inradius = outradius / 1.25;

var color = d3.scale.category10();

var svg = d3.select('#chart')
  .append('svg')
  .attr('width', width)
  .attr('height', height)
  .append('g')
  .attr('transform', 'translate(' + (width / 2) + ',' + (height / 2) + ')');
var arc = d3.svg.arc()
  .outerRadius(outradius)
  .innerRadius(inradius);
var pie = d3.layout.pie()
  .value(function(d, i) {
    console.log(d)
    return d.Value;
  })
  .sort(null);
var path = svg.selectAll('path')
  .data(pie(newData))
  .enter()
  .append('path')
  .attr('d', arc)
  .attr('fill', function(d, i) {
    return color(d.data.Name);
  });
