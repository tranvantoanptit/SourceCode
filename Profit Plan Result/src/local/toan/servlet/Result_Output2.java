package local.toan.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Result_Output
 */
@WebServlet("/Result_Output2")
public class Result_Output2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Result_Output2() {
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
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		response.setContentType("text/html");
		/*request.getRequestDispatcher("inquiry.jsp").include(request, response);*/
		//request.getRequestDispatcher("result_output_chart.jsp").include(request, response);
		PrintWriter pw = response.getWriter();
		String [][] chartData = null;
		String table = "";
		String js_array_chartData_Str = "[[";
		String jsonData = "";
		
		String selected_orgs = request.getParameter("org_list");
		String selected_expenses = request.getParameter("expense_list");
		String from_YM = request.getParameter("targetYM_from");
		String to_YM = request.getParameter("targetYM_to");
		
/*		pw.print("<p>"+selected_orgs+"</p>");
		pw.print("<p>"+selected_expenses+"</p>");
		pw.print("<p>"+from_YM+"</p>");
		pw.print("<p>"+to_YM+"</p>");
*/
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
			
			String targetyearmonth_list = "";
			String result_query_sql="SELECT targetyearmonth, sum(expensevalue) FROM result WHERE orgcode IN ("+selected_orgs+") "
					+ "AND expensecode IN ("+selected_expenses+") AND makedate IN (SELECT DISTINCT max(makedate) "
							+ "FROM result  where targetyearmonth BETWEEN '"+from_YM+"' "
									+ "AND '"+to_YM+"' GROUP BY targetyearmonth, orgcode ORDER BY 1 ASC) GROUP BY 1;";



			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(result_query_sql) ;
			
			rs.last();
			int size = rs.getRow() +1;
			rs.beforeFirst();
			
			//Create chartData array for chart Array
			chartData = new String[7][size];
			
			table += "<table border= '1px'><tr><th>TargetYM</th>";
			
			chartData[0][0] = "'Target YM'";
			
			int i = 1;
		    while(rs.next()){
		    	chartData[0][i] = "'"+rs.getString(1)+"'";
		    	pw.print("<th>"+rs.getString(1)+"</th>");
		    	i++;
		    }
		    
		    chartData[1][0] = "'Result'";
		    
		    i = 1;
		    table += "</tr><tr><th>Result</th>";
		    rs.beforeFirst();
		    LinkedList<Float> result = new LinkedList<Float>();
			while(rs.next()){
		    	targetyearmonth_list = targetyearmonth_list + "'" + rs.getString(1)+"',";
		    	table += "<td>"+rs.getString(2)+"</td>";
		    	result.add(rs.getFloat(2));
		    	chartData[1][i] = rs.getString(2);
		    	i++;
		    }
			Iterator<Float> result_itr = result.iterator();
			
		    targetyearmonth_list = targetyearmonth_list.substring(0, targetyearmonth_list.length()-1);

			String plan_query_sql = "SELECT sum(expensevalue) from plan where orgcode IN ("+selected_orgs+") "
									+ "AND expensecode IN ("+selected_expenses+") "
									+ "AND targetyearmonth IN ("+targetyearmonth_list+") GROUP BY targetyearmonth;";
			
			chartData[2][0] = "'Plan'";
			i = 1;
			rs = stmt.executeQuery(plan_query_sql);
			table += "</tr><tr><th>Plan</th>";
		    LinkedList<Float> plan = new LinkedList<Float>();
		    while(rs.next()){
		    	table += "<td>"+rs.getString(1)+"</td>";
		    	plan.add(rs.getFloat(1));
		    	chartData[2][i] = rs.getString(1);
		    	i++;
		    }
		    Iterator<Float> plan_itr = plan.iterator();
		    
			String masterplan_query_sql = "SELECT sum(expensevalue) from masterplan where orgcode IN ("+selected_orgs+") "
		    								+ "AND expensecode IN ("+selected_expenses+") "
		    								+ "AND targetyearmonth IN ("+targetyearmonth_list+") GROUP BY targetyearmonth;";
			rs = stmt.executeQuery(masterplan_query_sql);
			
			chartData[3][0] = "'MP'";
			i = 1;
		    pw.print("</tr><tr><th>MP</th>");
		    LinkedList<Float> mp = new LinkedList<Float>();
		    float total_mp = 0;
		    while(rs.next()){
		    	table += "<td>"+rs.getString(1)+"</td>";
		    	mp.add(rs.getFloat(1));
		    	total_mp += rs.getFloat(1);
		    	chartData[3][i] = rs.getString(1);
		    	i++;
		    }
		    Iterator<Float> mp_itr = mp.iterator();
		    chartData[4][0] = "'Plan(%)'";
			i = 1;
			table += "</tr><tr><th>Plan(%)</th>";

		    while(result_itr.hasNext()&&plan_itr.hasNext()){
		    	chartData[4][i] = String.format("%.2f",(100*(float)result_itr.next()/(float)(plan_itr.next())));
		    	table += "<td>"+chartData[4][i]+"</td>";
		    	i++;
		    }
		    
		    result_itr = result.listIterator();
		    plan_itr = plan.listIterator();
		    
		    chartData[5][0] = "'MP(%)'";
			i = 1;
			table += "</tr><tr><th>MP(%)</th>";

		    while(result_itr.hasNext()&&mp_itr.hasNext()){
		    	chartData[5][i] = String.format("%.2f",(100*(float)result_itr.next()/(float)(mp_itr.next())));
		    	table += "<td>"+chartData[5][i]+"</td>";
		    	i++;
		    }
		    
		    result_itr = result.listIterator();
		    mp_itr = mp.listIterator();
		    chartData[6][0] = "'MP progressive(%)'";
			i = 1;
			table += "</tr><tr><th>MP progressive(%)</th>";
		    
		    float mp_progressive = 0;
		    while(result_itr.hasNext()&&mp_itr.hasNext()){
		    	mp_progressive = mp_progressive + (float)result_itr.next()/total_mp;
		    	chartData[6][i] = String.format("%.2f",100*mp_progressive);
		    	table += "<td>"+chartData[6][i]+"</td>";
		    	i++;
		    }
		    
		    //Convert java array to javascript array for chart
		    int col, row;
		    for (col = 0;col<chartData[0].length-1;col++) {
		    	for (row=0; row<7;row++) {
		    		if (row ==  6) 
		    			js_array_chartData_Str = js_array_chartData_Str + chartData[row][col]+"],[";
		    		else
		    			js_array_chartData_Str = js_array_chartData_Str +chartData[row][col]+",";
		    	}
		    }

		    for (row=0; row<7;row++) {
		    	if (row ==  6) 
		    		js_array_chartData_Str = js_array_chartData_Str + chartData[row][col]+"]]";
		    	else
		    		js_array_chartData_Str = js_array_chartData_Str +chartData[row][col]+",";
		    }
	    		
		    /*request.setAttribute("js_array_chartData_Str", js_array_chartData_Str);*/
			conn.close();
			table += "</tr></table>";
		}catch(Exception e){
			table += "</tr><tr><th>Error</th><td>"+e+"</td>";
			table += "</tr></table>";
		}
		
		jsonData = "{\"tableStr\":\"" + table + "\"," + "\"chartDataStr\":\"" + js_array_chartData_Str + "\"}";
        pw.print(jsonData);
        
		pw.close();
	}

}
