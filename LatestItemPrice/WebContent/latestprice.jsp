<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<script type="text/javascript" src="./js/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="./js/createtable.js"></script>
	<script type="text/javascript" src="./js/jquery.table2excel.js"></script>
	<style>
        table, th, td 
        {
            margin:10px 0;
            border:solid 1px #333;
            padding:2px 4px;
            font:15px Verdana;
        }
        th {
            font-weight:bold;
        }
    </style>
	<script>
	$(document).ready(function(){
		$("#getlatestpricelist").click(function(){
			$.ajax({
				/* url : 'Result_Output2', */
				url : 'ExportLatestPriceList',
				data : {},
				type: 'post',
		        success : function(data) {
		        	var json_table_array = eval(data);
		        	$.getScript("./js/createtable.js",CreateTableFromJSON(json_table_array,"table_result","table_result_id"));        
		        }});
		});

// Export HTML table to excel for non IE browsers (Firefox, Chrome, Edge)
/* 		$("#exportlatestpricelist").click(function(){
			  $("#table_result_id").table2excel({
			    // exclude CSS class
			    exclude: ".noExl",
			    name: "LatestItemPriceList",
			    filename: "LatestItemPriceList" //do not include extension
			  }); 
			}); */
				
	});
	</script>
	
	<script Language="javascript">
// Export HTML table to excel file in IE		
	function exportToExcelMsIe() {
		  
		var tab_text="<table border='2px'><tr bgcolor='#87AFC6'>";
		var textRange; var j=0;
		var tab = document.getElementById('table_result_id'); // id of table

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
		var sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"LatestItemPrice.xls");
	};
		
	</script>
</head>
<body>
<div>
<iframe id="txtArea1" style="display:none"></iframe>
<fieldset>
<legend style="color:blue;font-weight:bold;">Latest Item Price Process</legend>
	<table width = "100%"  border = "1px none" bgcolor = "#00FFFF">
		<tr>
			<td width="20%"><b>Select tsv file to Upload:</b></td>
			<td>
				<form action="upload" method="post" enctype="multipart/form-data">
					<input type="file" name="tsvfilepath">
					<input type="submit" value="Upload"><br/>
					<input type="radio" name="data_type" value="PKTF403" checked >PKTF403
					<input type="radio" name="data_type" value="PKTF602">PKTF602
				</form>
			</td>
		</tr>
		<tr>
			<td colspan = "2" align = "center">
				<button id="getlatestpricelist">Get Latest Item Price List</button>
				<button id="exportlatestpricelist" onClick="exportToExcelMsIe();">Export to Excel</button>	
			</td>
		</tr>
		<tr>
		</tr>
	</table>
</fieldset>
</div>
<hr/>
<div id="table_result">
</div>
</body>
</html>
