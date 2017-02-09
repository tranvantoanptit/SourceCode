<%@ page import="java.sql.*" %>
<%ResultSet orglevel3_rs =null;%>

<%ResultSet expensename_rs=null;%>

<HTML>
<HEAD>
  <TITLE>Result query</TITLE>
  <meta charset="utf-8">
  <title>jQuery UI Datepicker - Default functionality</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script type="text/javascript">
  $(document).ready(function() {
    $( "#datepicker" ).datepicker({dateFormat: 'yy/mm/dd'});
    $( "#datepicker2" ).datepicker({dateFormat: 'yy/mm/dd'}); 
  });
  </script>
</HEAD>

<BODY>

<%
    try{
//Class.forName("com.mysql.jdbc.Driver").newInstance();
	Class.forName("org.postgresql.Driver");
	Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
	String orglevel3_sql="SELECT DISTINCT orglevel3 FROM orgtree ORDER BY 1";
	String expensename_sql="SELECT expensecode,expensename FROM expense ORDER BY 1 ASC";
	/* String result_inquiry_sql=null; */
    Statement statement = conn.createStatement() ;
    Statement statement2 = conn.createStatement() ;
    /* PreparedStatement pre_stmt=conn.prepareStatement(result_inquiry_sql); */
    orglevel3_rs =statement.executeQuery(orglevel3_sql) ;
    expensename_rs = statement2.executeQuery(expensename_sql);
%>
<form id="query_form" action="Result_Output1" method="post">
        Org_CD:<select  multiple="true" name="orglevel3">
        <%  while(orglevel3_rs.next()){ %>
            <option value=<%= orglevel3_rs.getString(1)%>><%= orglevel3_rs.getString(1)%></option>
        <% } %>
        </select>
        Expense:<select multiple="multiple" name="expenselist">
        <%  while(expensename_rs.next()){ %>
            <option value=<%= expensename_rs.getString(1)%>><%= expensename_rs.getString(1)+':'+expensename_rs.getString(2)%></option>
        <% } %>
        </select>
        From DT: <input type="text" id="datepicker" name="from_DT"/>
        To DT: <input type="text" id="datepicker2" name="to_DT"/>
        <!-- <center><input type="submit" value="Search"/></center> -->
        <!-- <input type="submit" onclick="get_selected_items('orglevel3');get_selected_items('expenselist');" value="Search"> --> 
        <br><center><input type="submit"  value="Search"></center> 
        <% conn.close();%>
<!--         <input type="hidden" name="selected_org_Str" value="">
        <input type="hidden" name="selected_expense_Str" value=""> -->
        
</form>
<%
        }
        catch(Exception e)
        {
             out.println("wrong entry"+e);
        }
%>
<hr>
</BODY>
</HTML>