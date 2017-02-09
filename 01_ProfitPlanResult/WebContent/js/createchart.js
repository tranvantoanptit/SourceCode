function drawChart(chartData, chartcontainer){
	/* alert(chartData); */
	/* chartDataArray = eval(chartData); */
	var dataChart = google.visualization.arrayToDataTable(eval(chartData));
	var options = {
		title: 'Result chart',
    	vAxes: {0: {title: 'Expense',},
    /*			1: {title: '%',},*/
    	},
		hAxis: {title: 'TargetYM'},
		seriesType: 'line',
			/*series: {0:{targetAxisIndex:0},
				 1:{targetAxisIndex:0},
				 2:{targetAxisIndex:0}
				 3:{type:'line',targetAxisIndex:1},
				 4:{type:'line',targetAxisIndex:1},
				 5:{type:'line',targetAxisIndex:1}
				 }, */
	};
	var chart = new google.visualization.ComboChart(document.getElementById(chartcontainer));
   	chart.draw(dataChart, options);
}