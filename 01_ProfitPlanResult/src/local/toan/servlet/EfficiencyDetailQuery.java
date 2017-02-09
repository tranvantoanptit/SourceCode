package local.toan.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EfficiencyDetailQuery
 */
@WebServlet("/EfficiencyDetailQuery")
public class EfficiencyDetailQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EfficiencyDetailQuery() {
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
		PrintWriter pw = response.getWriter();
		String table_data = "";
		String FromYM = request.getParameter("FromYM");
		String ToYM = request.getParameter("ToYM");
		String OrgCD = request.getParameter("OrgCD").replaceAll("\n", "\',\'");
		String EffAccCD = request.getParameter("EffAccCD").replaceAll("\n", "\',\'");
		String EffAccName = request.getParameter("EffAccName");
		String AccCD = request.getParameter("AccCD").replaceAll("\n", "\',\'");
		String AccName = request.getParameter("AccName");
		String DescRemark = request.getParameter("DescRemark");
		String where_clause = "";
		String total_amount = "";
		String inputCheck = FromYM.concat(ToYM.concat(OrgCD.concat(EffAccCD.concat(EffAccName.concat(AccCD.concat(AccName.concat(DescRemark)))))));
		//pw.print(inputCheck);
		
		String detail_sql= "SELECT array_to_json(array_agg(row_to_json(tc1))) FROM (SELECT row_number() OVER (ORDER BY f1,f3,f5,f7) "
				+ "AS no,f1 AS target_ym,f3 AS org_cd,f5 AS eff_acc_cd,f6 AS eff_acc_name,f7 AS acc_cd,f8 AS acc_name,"
				+ "f17 AS description,to_char(f13,'999G999G999D99') AS amount "
				+ "FROM pktr031 ";
/*		String detail_sql= "SELECT array_to_json(array_agg(row_to_json(tc1))) FROM (SELECT f1 AS target_ym,f3 AS org_cd,f4 AS org_name,f5 AS eff_acc_cd,f6 AS eff_acc_name,f7 AS acc_cd,f8 AS acc_name,"
				+ "f17 AS description,f13 AS amount "
				+ "FROM pktr031 ";*/		
		
		if(inputCheck != "") {
			where_clause += "WHERE ";
			if(FromYM != "") {
				where_clause += "f1 >= '"+FromYM+"'";
			}
			
			if(ToYM != ""){
				where_clause += " AND f1 <= '"+ToYM+"'";
			}
			
			if(OrgCD != ""){
				if(OrgCD.length() < 8 ){
					where_clause += " AND f3 ILIKE '%"+OrgCD+"%'";
				}
				else
					where_clause += " AND f3 IN ('"+OrgCD+"')";
			}
			
			if(EffAccCD != ""){
				where_clause += " AND f5 IN ('"+EffAccCD+"')";
			}
			
			if(EffAccName != ""){
				where_clause += " AND f6 ILIKE '%"+EffAccName+"%'";
			}
			
			if(AccCD != ""){
				where_clause += " AND f7 IN ('"+AccCD+"')";
			}
			
			if(AccName != ""){
				where_clause += " AND f8 ILIKE '%"+AccName+"%'";
			}
			
			if(DescRemark != ""){
				where_clause += " AND f17 ILIKE '%"+DescRemark+"%'";
			}
			
		}
		
		detail_sql = detail_sql + where_clause +" ORDER BY 1,2,5) tc1;";			
		
		String total_amount_sql = "SELECT SUM(f13) FROM pktr031 " + where_clause;
		System.out.print(detail_sql+"\n");
		
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn;
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
			
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(detail_sql) ;
			
			while(rs.next()){	
				if(rs.getString(1) == null){
					table_data = "[{\"target_ym\":\"\",\"org_cd\":\"\",\"org_name\":\"\",\"eff_acc_cd\":\"\",\"eff_acc_name\":\"\","
							+ "\"acc_cd\":\"\",\"acc_name\":\"\",\"amount\":\"\",\"description\":\"\"}]";
				}
				else{
					table_data = rs.getString(1).replaceAll("\\[\\[","").replaceAll("]]", "").replace("\\\"", "");
				}
			}
			
			rs = stmt.executeQuery(total_amount_sql) ;
			while(rs.next()){
				total_amount = rs.getString(1);
				System.out.println("Total_Amount: "+total_amount);
			}	
			conn.close();
		}catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			pw.print(e);
		}
		pw.print(table_data+"|"+total_amount);	
		pw.close();
	}
 
}
