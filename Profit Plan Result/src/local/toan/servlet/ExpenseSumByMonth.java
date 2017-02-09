package local.toan.servlet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class test
 */
@WebServlet("/ExpenseSumByMonth")
public class ExpenseSumByMonth extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExpenseSumByMonth() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String chart_data_array = "";
		String table_data_array = "";
		String jsondata = "";
		String selected_orgs = request.getParameter("org_list");
		String selected_expenses = request.getParameter("expense_list");
		String from_YM_tmp = request.getParameter("targetYM_from");
		String from_YM = from_YM_tmp.substring(0,4)+"/"+from_YM_tmp.substring(4);
		String to_YM_tmp = request.getParameter("targetYM_to");
		String to_YM = to_YM_tmp.substring(0,4)+"/"+to_YM_tmp.substring(4);
		
		String result_query_sql="SELECT '\"' || expensecode || ':' || expensename || '\"' as Expense, '\"' || targetyearmonth || '\"' as targetyearmonth, sum(expensevalue) "
								+"AS result_sum INTO TEMP TABLE tmp FROM result "
								+"WHERE orgcode IN ("+selected_orgs+") AND expensecode IN ("+selected_expenses+") "
								+"AND makedate IN (SELECT DISTINCT max(makedate) "
								+"FROM result WHERE targetyearmonth BETWEEN '"+from_YM+"' AND '"+to_YM+"' "
								+"GROUP BY targetyearmonth, orgcode ORDER BY 1 ASC) GROUP BY 1,2 ORDER BY 1,2 ASC;";
		String aSQLScriptFilePath1 = "C:/Temp/sql/get_result_sum1.sql";
		String aSQLScriptFilePath2 = "C:/Temp/sql/get_result_sum2.sql";
/*		String aSQLScriptFilePath1 = "C:/Users/ToanTran/workspace/Profit Plan Result/WebContent/sql/get_result_sum1.sql";
		String aSQLScriptFilePath2 = "C:/Users/ToanTran/workspace/Profit Plan Result/WebContent/sql/get_result_sum2.sql";*/
		FileWriter fw1 = new FileWriter(aSQLScriptFilePath1);
		fw1.write("DROP TABLE IF EXISTS tmp;\n");
		fw1.write(result_query_sql+"\n");
		fw1.write("SELECT pivotcode('tmp','Expense','targetyearmonth','result_sum','numeric');\n");
		fw1.write("SELECT pivotcode('tmp','targetyearmonth','Expense','result_sum','numeric');");
		fw1.close();
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String out ="";
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection conn;
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
			// Creating object of ScriptRunner class
			ScriptRunner scriptRunner = new ScriptRunner(conn, false, true);

			// Executing SQL Script
			scriptRunner.runScript(new FileReader(aSQLScriptFilePath1));

			// Optional Part...
			List<Table> tableList; // Used to store result of 'SELECT' Query execution
			//List<String> sqlOutput; // Used to store result of any quires except 'SELECT' quires

			tableList = scriptRunner.getTableList();
			//sqlOutput = scriptRunner.getSqlOutput();
			
			FileWriter fw2 = new FileWriter(aSQLScriptFilePath2);
			
			String out1 = tableList.get(0).getToupleList().toString().replaceAll("\\*", "\\* into temp table t1 ");
				   out1 = out1.substring(2,out1.length()-2);
			String out2 = tableList.get(1).getToupleList().toString().replaceAll("\\*", "\\* into temp table t2 ");
				   out2 = out2.substring(2,out2.length()-2);
		    String out3 = tableList.get(1).getToupleList().toString();
				   out3 = out3.substring(2,out3.length()-2);

			fw2.write(out1+"\n");
			fw2.write(out2+"\n");
			fw2.write(out3+"\n");
			
			fw2.write("SELECT array_to_json(array_agg(row_to_json(tc1))) FROM (select * from t1) tc1;\n");
			fw2.write("SELECT array_to_json(array_agg(row_to_json(tc2))) FROM (select * from t2) tc2;");
			
			/*fw2.write("SELECT array_agg(tc2)  FROM (SELECT column_name FROM information_schema.columns WHERE table_name ='t2') tc2;");*/
			
			fw2.close();
			scriptRunner.runScript(new FileReader(aSQLScriptFilePath2));
			
			tableList = scriptRunner.getTableList();

			chart_data_array = tableList.get(4).getToupleList().toString().replaceAll("\\[\\[","").replaceAll("]]", "").replace("\\\"", "");
			table_data_array = tableList.get(3).getToupleList().toString().replaceAll("\\[\\[","").replaceAll("]]", "").replace("\\\"", "");
			/*chart_data_array = tableList.get(2).getToupleList().toString().replaceAll("\\[\\[","\\[").replaceAll("]]", "]");
			column_name_row = tableList.get(4).getToupleList().toString().replaceAll("\\[\\[\\{", "\\[").replaceAll("}]]", "]").replaceAll("\\(", "").replaceAll("\\)", "");
			chart_data_array = "[" + column_name_row +","+ chart_data_array + "]";
			chart_data_array = chart_data_array.replaceAll("\"", "'").replaceAll("\\\\'", "").replaceAll("targetyearmonth","'targetyearmonth'");*/
				  
			conn.close();
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				pw.print(out+e);
			}
		
			jsondata = table_data_array + "|" +chart_data_array;
			pw.print(jsondata);
			pw.close();
			
	}



}
