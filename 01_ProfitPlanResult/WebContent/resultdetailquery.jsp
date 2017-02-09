<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="stylesheet" type="text/css" href="./css/datatables.min.css">
	<link rel="stylesheet" type="text/css" href="./css/table.css">
	<link rel="stylesheet" type="text/css" href="./css/layout.css">
	<script type="text/javascript" src="./js/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="./js/createtablewithfixedheader.js"></script>
	<script type="text/javascript" src="./js/datatables.min.js"></script>
<!-- 	<script type="text/javascript" src="./js/jquery.dataTables.min.js"></script> -->
	
	<title>Efficiency Detail Query</title>
	<%@ include file="header_nav.jsp" %>
</head>
<body>
	<table class="tablenoborder">
		<tr class="noborder">
			<td colspan="8" class="noborder"><b>EFFICIENCY DETAIL INQUIRY</b></td>
		</tr>
	</table>
	<table>
		<tr>
			<td class="label">Target YM:</td>
			<td colspan="7">From:<input type="text" name="FromYM" id="FromYM">To:<input type="text" name="ToYM" id="ToYM"></td>
		</tr>
		<tr>
			<td class="label">ORG CD:</td>
			<td colspan="7">
				<textarea rows="1" name="OrgCD" id="OrgCD"></textarea>
				<input type="button" value="..." id="OrgBtn">
			</td>
		</tr>
		<tr>
			<td class="label">Efficiency Account CD:</td>
			<td>
				<textarea rows="1" name="EffAccCD" id="EffAccCD"></textarea>
				<input type="button" value="..." id="EffAccCdBtn">
			</td>
			<td class="label">Efficiency Account Name:</td>
			<td>
				<input type="text" name="EffAccName" id="EffAccName" size="50">
				<input type="button" value="..." id="EffAccNameBtn">
			</td>
		</tr>
		<tr>
			<td class="label">Account CD:</td>
			<td>
				<textarea rows="1" name="AccCD" id="AccCD"></textarea>
				<input type="button" value="..." id="AccCDBtn">
			</td>
			<td class="label">Account Name:</td>
			<td>
				<input type="text" name="AccName" id="AccName" size="50">
				<input type="button" value="..." id="AccNameBtn">
			</td>
		</tr>
		<tr>
			<td class="label">Description/Remarks:</td>
			<td colspan="7"><input type="text" name="DescRemark" id="DescRemark" size="57"></td>
		</tr>
	</table>
	
	<table class="tablenoborder" id="button">
		<tr class="noborder">
			<td class="noborder">
				<input type="button" value="Search" id="Search" OnClick="EfficiencyQuery();" style="width: 6em;">
				<iframe id="txtArea1" style="display:none"></iframe>
				<input type="button" value="Download" id="DownLoadBtn" onClick="exportToExcelMsIE();" style="width: 6em;">
			</td>
			<td class="noborder"></td>
			<td align="right" class="noborder" width="100%">
				<input type="button" value="Previous" id="PreviousBtn" style="width: 6em;">
				<input type="button" value="Next" id="NextBtn" style="width: 6em;">
			</td>
		</tr>
	</table>
	<table class="tablenoborder">
		<tr class="noborder">
			<td class="noborder" width="100%"></td>
			<td class="label" class="noborder">Total Amount:</td>
			<td id="TotalAmount"></td>
		</tr>
	</table>
	
	<hr/>
	<div style="margin-right: 10px;">
	<table id="table_result" style="width: 100%">
	<thead>
		<tr>
			<th width="4%">No.</th>
			<th width="6.5%">Target YM</th>
			<th width="6%">Org. CD</th>
			<th width="7%">Eff Acc. CD</th>
			<th width="16%">Eff Acc. Name</th>
			<th width="5%">Acc. CD</th>
			<th width="16%">Acc. Name</th>
			<th>Description</th>
			<th width="6%">Amount</th>
		</tr>
	</thead>
	
	</table>
	</div>
<!-- 	<div id="table_result"></div> -->
	<!-- <footer>Copyright 2016 - edp1@kvc.kyocera.com.vn</footer> -->
	
	<script>
		function EfficiencyQuery(){
			$('#table_result').DataTable({
			processing: true,	
			serverSide: true,
			ajax: {
				url: 'EfficiencyDetailQuery',
				data: {
					FromYM: $('#FromYM').val(),
					ToYM: $('#ToYM').val(),
					OrgCD: $('#OrgCD').val(),
					EffAccCD: $('#EffAccCD').val(),
					EffAccName: $('#EffAccName').val(),
					AccCD: $('#AccCD').val(),
					AccName: $('#AccName').val(),
					DescRemark: $('#DescRemark').val()
				},
				type: 'post',
				success: function(data){
					var data_arr = data.split("|");
	            	var json_table_array = eval(data_arr[0]);
	            	$.getScript("./js/createtablewithfixedheader.js",CreateTableFromJSON(json_table_array,"table_result"));
	               	if (data_arr[1] == "null"){
	            		document.getElementById("TotalAmount").innerHTML = '0';
	            	}
	            	else{
	            		document.getElementById("TotalAmount").innerHTML = addCommas(data_arr[1]);
	            	}
				},
			}
			})
		};
	</script>
	<script>
		function exportToExcelMsIE() {
			  
			var tab_text="<table border='2px'><tr bgcolor='#87AFC6'>";
			var textRange; var j=0;
			var tab = document.getElementById('table_result'); // id of table

			for(j = 0 ; j < tab.rows.length ; j++) 
			{     
				tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
			}

			tab_text=tab_text+"</table>";
			tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
			tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
			tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params


			var txtArea1 = document.getElementById('txtArea1');
			txtArea1.contentWindow.document.open("txt/html","replace");
			txtArea1.contentWindow.document.write(tab_text);
			txtArea1.contentWindow.document.close();
			txtArea1.contentWindow.focus(); 
			var sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"data.xls");
		};
	</script>
	<script type="text/javascript">
		function addCommas(nStr)
		{
		nStr += '';
		var x = nStr.split('.');
		var x1 = x[0];
		var x2 = x.length > 1 ? '.' + x[1] : '';
		var rgx = /(\d+)(\d{3})/;
		while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
		}
		return x1 + x2;
		}
	</script>
	
<!-- 	<script type="text/javascript">
	function CommaFormatted(amount) {
		var delimiter = ","; // replace comma if desired
		var a = amount.split('.',2)
		var d = a[1];
		var i = parseInt(a[0]);
		if(isNaN(i)) { return ''; }
		var minus = '';
		if(i < 0) { minus = '-'; }
		i = Math.abs(i);
		var n = new String(i);
		var a = [];
		while(n.length > 3) {
			var nn = n.substr(n.length-3);
			a.unshift(nn);
			n = n.substr(0,n.length-3);
		}
		if(n.length > 0) { a.unshift(n); }
		n = a.join(delimiter);
		if(d.length < 1) { amount = n; }
		else { amount = n + '.' + d; }
		amount = minus + amount;
		return amount;
	}
	</script> -->
</body>
</html>