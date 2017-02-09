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
@WebServlet("/Result_Output")
public class Result_Output extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Result_Output() {
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
		
		String[] selected_org = request.getParameterValues("orglevel3");
		String[] selected_expense = request.getParameterValues("expenselist");
		
		String from_DT = request.getParameter("from_DT");
		String to_DT = request.getParameter("to_DT");
		
		/*pw.print(from_DT + "<br>");
		pw.print(to_DT + "<br>");*/
		String selected_org_list = "";
		String selected_expense_list = "";
		
		for(int i=0;i<selected_org.length;i++){
			if(i==(selected_org.length-1))
				selected_org_list = selected_org_list + "'" + selected_org[i] + "'" ;
			else
				selected_org_list = selected_org_list +  "'" + selected_org[i] + "'" +",";
		}
		
		/*pw.print(selected_org_list+"<br>");*/
		for(int j=0;j<selected_expense.length;j++){
			if(j==(selected_expense.length-1))
				selected_expense_list = selected_expense_list + "'" + selected_expense[j] + "'";
			else
				selected_expense_list = selected_expense_list + "'" + selected_expense[j] + "'" + ",";
		}
		/*pw.print(selected_expense_list);*/
	
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
			
			String targetyearmonth_list = "";
			String result_query_sql="SELECT targetyearmonth, sum(expensevalue) FROM result WHERE orgcode IN ("+selected_org_list+") "
					+ "AND expensecode IN ("+selected_expense_list+") AND makedate IN (SELECT DISTINCT max(makedate) "
							+ "FROM result  where makedate BETWEEN '"+from_DT+"' "
									+ "AND '"+to_DT+"' GROUP BY targetyearmonth, orgcode ORDER BY 1 ASC) GROUP BY 1;";



			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(result_query_sql) ;
			
			rs.last();
			int size = rs.getRow() +1;
			rs.beforeFirst();
			
			//Create chartData array for chart Array
			chartData = new String[7][size];
			
			pw.print("<table border= '1px'><tr><th>TargetYM</th>");
			
			chartData[0][0] = "'Target YM'";
			
			int i = 1;
		    while(rs.next()){
		    	chartData[0][i] = "'"+rs.getString(1)+"'";
		    	pw.print("<th>"+rs.getString(1)+"</th>");
		    	i++;
		    }
		    
		    chartData[1][0] = "'Result'";
		    
		    i = 1;
		    pw.print("</tr><tr><th>Result</th>");
		    rs.beforeFirst();
		    LinkedList<Float> result = new LinkedList<Float>();
			while(rs.next()){
		    	targetyearmonth_list = targetyearmonth_list + "'" + rs.getString(1)+"',";
		    	pw.print("<td>"+rs.getString(2)+"</td>");
		    	result.add(rs.getFloat(2));
		    	chartData[1][i] = rs.getString(2);
		    	i++;
		    }
			Iterator<Float> result_itr = result.iterator();
			
		    targetyearmonth_list = targetyearmonth_list.substring(0, targetyearmonth_list.length()-1);

			String plan_query_sql = "SELECT sum(expensevalue) from plan where orgcode IN ("+selected_org_list+") "
									+ "AND expensecode IN ("+selected_expense_list+") "
									+ "AND targetyearmonth IN ("+targetyearmonth_list+") GROUP BY targetyearmonth;";
			
			chartData[2][0] = "'Plan'";
			i = 1;
			rs = stmt.executeQuery(plan_query_sql);
		    pw.print("</tr><tr><th>Plan</th>");
		    LinkedList<Float> plan = new LinkedList<Float>();
		    while(rs.next()){
		    	pw.print("<td>"+rs.getString(1)+"</td>");
		    	plan.add(rs.getFloat(1));
		    	chartData[2][i] = rs.getString(1);
		    	i++;
		    }
		    Iterator<Float> plan_itr = plan.iterator();
		    
			String masterplan_query_sql = "SELECT sum(expensevalue) from masterplan where orgcode IN ("+selected_org_list+") "
		    								+ "AND expensecode IN ("+selected_expense_list+") "
		    								+ "AND targetyearmonth IN ("+targetyearmonth_list+") GROUP BY targetyearmonth;";
			rs = stmt.executeQuery(masterplan_query_sql);
			
			chartData[3][0] = "'MP'";
			i = 1;
		    pw.print("</tr><tr><th>MP</th>");
		    LinkedList<Float> mp = new LinkedList<Float>();
		    float total_mp = 0;
		    while(rs.next()){
		    	pw.print("<td>"+rs.getString(1)+"</td>");
		    	mp.add(rs.getFloat(1));
		    	total_mp += rs.getFloat(1);
		    	chartData[3][i] = rs.getString(1);
		    	i++;
		    }
		    Iterator<Float> mp_itr = mp.iterator();
		    chartData[4][0] = "'Plan(%)'";
			i = 1;
		    pw.print("</tr><tr><th>Plan(%)</th>");

		    while(result_itr.hasNext()&&plan_itr.hasNext()){
		    	chartData[4][i] = String.format("%.2f",(100*(float)result_itr.next()/(float)(plan_itr.next())));
		    	pw.print("<td>"+chartData[4][i]+"</td>");
		    	i++;
		    }
		    
		    result_itr = result.listIterator();
		    plan_itr = plan.listIterator();
		    
		    chartData[5][0] = "'MP(%)'";
			i = 1;
		    pw.print("</tr><tr><th>MP(%)</th>");

		    while(result_itr.hasNext()&&mp_itr.hasNext()){
		    	chartData[5][i] = String.format("%.2f",(100*(float)result_itr.next()/(float)(mp_itr.next())));
		    	pw.print("<td>"+chartData[5][i]+"</td>");
		    	i++;
		    }
		    
		    result_itr = result.listIterator();
		    mp_itr = mp.listIterator();
		    chartData[6][0] = "'MP progressive(%)'";
			i = 1;
		    pw.print("</tr><tr><th>MP progressive(%)</th>");
		    
		    float mp_progressive = 0;
		    while(result_itr.hasNext()&&mp_itr.hasNext()){
		    	mp_progressive = mp_progressive + (float)result_itr.next()/total_mp;
		    	chartData[6][i] = String.format("%.2f",100*mp_progressive);
		    	pw.print("<td>"+chartData[6][i]+"</td>");
		    	i++;
		    }
		    
		    //Convert java array to javascript array
		    int col, row;
		    String js_array_Str = "[[";
		    for (col = 0;col<chartData[0].length-1;col++) {
		    	for (row=0; row<7;row++) {
		    		if (row ==  6) 
		    			js_array_Str = js_array_Str + chartData[row][col]+"],[";
		    		else
		    			js_array_Str = js_array_Str +chartData[row][col]+",";
		    	}
		    }

		    for (row=0; row<7;row++) {
		    	if (row ==  6) 
		    		js_array_Str = js_array_Str + chartData[row][col]+"]]";
		    	else
		    		js_array_Str = js_array_Str +chartData[row][col]+",";
		    }
	    		
		    request.setAttribute("js_array_Str", js_array_Str);
		    conn.close();
			
		}catch(Exception e){
			pw.print("</tr><tr><th>Error</th><td>"+e+"</td>");
			pw.print("</tr></table>");
		}
		pw.close();
		request.getRequestDispatcher("result_output_chart.jsp").include(request, response);
	}

}
