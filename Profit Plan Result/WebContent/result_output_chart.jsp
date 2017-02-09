<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%-- <%@ page import="local.toan.servlet.Result_Output"%> --%>
<%@ include file="inquiry.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result output query</title>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
   	<script type="text/javascript">
     google.charts.load('current', {packages: ['corechart']});  
     google.charts.setOnLoadCallback(drawChart);
	<% String chartData = (String)request.getAttribute("js_array_Str"); %>

		function drawChart(){
			var data = google.visualization.arrayToDataTable(<%= chartData%>);
			var options = {
				title: 'Result chart',
		        vAxes: {0: {title: 'Expense',
/* 		                    viewWindow:{
		                                max:000,
		                                min:0
		                                }, */
		                    /* gridlines: {color: 'transparent'}, */
	                    },
                		1: {title: '%',
/* 		                     viewWindow:{
		                                  max:100,
		                                  min:0
		                                   }, */
		                     /* gridlines: {color: 'transparent'}, */
	                     },
                },
				hAxis: {title: 'TargetYM'},
				seriesType: 'bars',
				series: {0:{targetAxisIndex:0},
						 1:{targetAxisIndex:0},
						 2:{targetAxisIndex:0}
						 },
				series: {3:{type:'line',targetAxisIndex:1},
						 4:{type:'line',targetAxisIndex:1},
						 5:{type:'line',targetAxisIndex:1}
						 },
			};
			var chart = new google.visualization.ComboChart(document.getElementById('container'));
			   chart.draw(data, options);
		}
	 </script>
</head>
<body>
    <div id="container"></div>
	<p id="demo"></p>
</body>
</html>