<%@ page import="java.sql.*" %>
<%ResultSet org_rs =null;%>
<%ResultSet expense_rs =null;%>

<!DOCTYPE HTML>
<HTML>
<HEAD>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="stylesheet" type="text/css" href="./css/jqx.base.css" />
	<link rel="stylesheet" type="text/css" href="./css/easyui.css">
	<link rel="stylesheet" type="text/css" href="./css/table.css">
	<link rel="stylesheet" type="text/css" href="./css/layout.css">
	
	<script type="text/javascript" src="./js/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="./js/jqxcore.js"></script>
	<script type="text/javascript" src="./js/jqxtree.js"></script>
	<script type="text/javascript" src="./js/jqxcheckbox.js"></script>
	<script type="text/javascript" src="./js/jqxbuttons.js"></script>
	<script type="text/javascript" src="./js/jqxscrollbar.js"></script>
	<script type="text/javascript" src="./js/jqxlistbox.js"></script>
	<script type="text/javascript" src="./js/builddata.js"></script>
	<script type="text/javascript" src="./js/google_chart_loader.js"></script>
	<script type="text/javascript" src="./js/createtablewithunfixedheader.js"></script>
	<script type="text/javascript" src="./js/highcharts.js"></script>
	<script type="text/javascript" src="./js/jquery.highchartsmaker.js"></script>
	
	<script src="./js/data.js"></script>
	<script src="./js/exporting.js"></script>
	
	<%
	
    String org_tree = "";
    String expense_list = "";
    try{
		Class.forName("org.postgresql.Driver");
		Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
		String org_sql="SELECT array_to_json(array_agg(row_to_json(t)))"
						+"FROM (SELECT org_cd,upper_rank_org_cd FROM organization WHERE target_dt='201609' ORDER BY org_rank,org_cd) t"; //Need to modify to get target_dt = current month
/* 						+"FROM (SELECT org_cd,upper_rank_org_cd FROM organization WHERE target_dt='201609' ORDER BY org_rank,1, org_cl, output_order) t"; */  //Need to modify to get target_dt = current month
		String expensename_sql="SELECT expensecode,expensename FROM expense ORDER BY 2 ASC";
		Statement statement = conn.createStatement() ;
		Statement statement2 = conn.createStatement() ;
	    org_rs =statement.executeQuery(org_sql) ;
	    expense_rs = statement2.executeQuery(expensename_sql);
	    while(org_rs.next())
			org_tree=org_rs.getString(1);
	    while(expense_rs.next())
	    	expense_list += ",\"" + expense_rs.getString(1) + ":" + expense_rs.getString(2) + "\"";
	    conn.close();
	    expense_list = "[" + expense_list.substring(1) + "]";
	    }catch(Exception e){out.println("wrong entry"+e);}

	    %>
	<script type="text/javascript">
		$(document).ready(function () {
		/* $('#article').hide();	 */
		var data = <%= org_tree%>;
		var source_expense = <%= expense_list%>;
		source_expense.splice(0, 0, '(Select All)');
		// Create a jqxListBox
	    $("#listbox").jqxListBox({width: 180, height: 200, source: source_expense, checkboxes: true});
		
        var handleCheckChange = true;
        $("#listbox").on('checkChange', function (event) {
            if (!handleCheckChange)
                return;

            if (event.args.label != '(Select All)') {
                handleCheckChange = false;
                $("#listbox").jqxListBox('checkIndex', 0);
                var checkedItems = $("#listbox").jqxListBox('getCheckedItems');
                var items = $("#listbox").jqxListBox('getItems');

                if (checkedItems.length == 1) {
                    $("#listbox").jqxListBox('uncheckIndex', 0);
                }
                else if (items.length != checkedItems.length) {
                    $("#listbox").jqxListBox('indeterminateIndex', 0);
                }
                handleCheckChange = true;
            }
            else {
                handleCheckChange = false;
                if (event.args.checked) {
                    $("#listbox").jqxListBox('checkAll');
                }
                else {
                    $("#listbox").jqxListBox('uncheckAll');
                }

                handleCheckChange = true;
            }
        });
        
		var source = builddata(data);
		// create jqxTree
		$('#jqxTree').jqxTree({ source: source, width: 180, hasThreeStates: true, checkboxes: true});
		});
		

		/* ============================================================================================== */
		
		function getChecked(){
		$('#article').show();		
		//get all selected expenses
		var checked_expenses = $("#listbox").jqxListBox('getCheckedItems');
        var checked_expense_list = '';
        	$.each(checked_expenses, function (index) {
        		if (index < checked_expenses.length - 1) {
        			checked_expense_list += "'" + this.label.substring(0,4) + "',";
        		}
        		else checked_expense_list += "'" + this.label.substring(0,4) + "'";
        	});
        	/* document.getElementById("display_selected_orgs").innerHTML = checked_expense_list; */
	    // get all checked organizations
	    
  
	    var checked_orgs = $("#jqxTree").jqxTree('getCheckedItems');
	    // save all checked organizations in checkedOrgaization array.
	    var checked_org_array = new Array();
		var checked_org_list = '';
		$.each(checked_orgs, function () {{
	        if (this.hasItems == false) {
	        	checked_org_array.push(this.label);
	        	checked_org_list += "'" + this.label + "',";
	        }
	        
	    }
		
/* 	    $.each(checked_orgs, function () {
	        if (this.checked) {
	        	checked_org_array[checked_org_array.length] = this.label;
									checked_org_list += "'" + this.label + "',";
	        } */
	    });
	    
	    checked_org_list = checked_org_list.substring(0,checked_org_list.length-1);
	    /* document.getElementById("display_selected_exps").innerHTML = checked_org_list; */
 	    if (checked_expense_list === ''){
	    	alert("Please select expense!");
	    }
	    if (checked_org_list === '') {
			alert("Please select organization!");
		}
  	    $('#org_list').val(checked_org_list);
   		$('#expense_list').val(checked_expense_list);
   	/* =======================================================================================	 */
		$.ajax({
			/* url : 'Result_Output2', */
			url : 'ExpenseSumByMonth',
			data : {
				org_list: checked_org_list,
				expense_list: checked_expense_list,	
				targetYM_from: $('#from_YM').val(),
				targetYM_to: $('#to_YM').val()
			},
			type: 'post',
            success : function(data) {
            	var data_arr = data.split("|"); 
            	/* $('#test').val(data_arr[1]); */
            	var json_table_array = eval(data_arr[0]);
            	$.getScript("./js/createtablewithunfixedheader.js",CreateTableFromJSON(json_table_array,"table_result","table_result_id"));
            	var json_chart_array = eval(data_arr[1]);
            	$.getScript("./js/createtable.js",CreateTableFromJSON(json_chart_array,"chart_result","hidden_chart_table_id"));
/*	=======================================================================================	 */
			$('#chart_result').highcharts({
		        data: {
		            table: 'hidden_chart_table_id'
		        },
		        chart: {
		            type: 'line'
		        },
		        title: {
		            text: 'Expense summary trend'
		        },
		        yAxis: {
		            allowDecimals: false,
		            title: {
		                text: 'Units'
		            }
		        },
/* 		        tooltip: {
		            formatter: function () {
		                return '<b>' + this.series.name + '</b><br/>' +
		                    this.point.y + ' ' + this.point.name.toLowerCase();
		            } */
		        tooltip: {
	                formatter: function() {
	                    return '<b>'+ this.series.name +'</b><br/>'+
	                        /* this.x +': '+ this.y; */
	                        this.y;
		        }
		        }
		        
		    });
			 alert("Query finised");
			}
		});
		}

	</script>
	<!-- Export HTML table to excel file in IE -->
	<script>
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
		var sa=txtArea1.contentWindow.document.execCommand("SaveAs",true,"data.xls");
	};
		
	</script>
<%@ include file="header_nav.jsp" %>

</HEAD>
<BODY>
<div id="container">
	<nav>
	<form id="query_form2">
		<input type="button" onclick="getChecked()" value="Search" id="Search">
		<p></p>
		<p><b>Target YM:</b></p>
		From :<input type="text" placeholder="YYYYMM" name="targetYM_from" id="from_YM" size="15" style="width: 130px">
		To :&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" placeholder="YYYYMM" name="targetYM_to" id="to_YM" size="15" style="width: 130px">
		<input type="hidden" name="org_list" id="org_list"/>
		<input type="hidden" name="expense_list" id="expense_list"/>
		<p></p>
		<label><b>Select Expense:</b></label>
		<div id="listbox"></div>
		<label><b>Select ORG_CD:</b></label>
		<div id='jqxTree'></div>
	</form>
	</nav>
	<article id="article">
	<iframe id="txtArea1" style="display:none"></iframe>
	<h2 id="table_label">Table:<button id="exporttable" onClick="exportToExcelMsIe();">Export</button></h2>
	<div id="table_result"></div>
	<h2 id="graph_label">Graph:</h2>
	<div id="chart_result">
	<table id="hidden_chart_table_id"></table>
	</div>
	<!-- <input type="text" id="test"> -->
	</article> 
	<footer>Copyright 2016 - edp1@kvc.kyocera.com.vn</footer>
</div>

</BODY>
</HTML>