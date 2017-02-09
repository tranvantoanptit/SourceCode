package local.toan.servlet;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class ExportLatestPriceList
 */
@WebServlet("/ExportLatestPriceList")
public class ExportLatestPriceList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportLatestPriceList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String table_data_array = "";
		ServletContext servletContext = getServletContext();
        String SqlScriptPath = servletContext.getRealPath("/resources/sql/getlatestprice.sql");
        
		FileReader reader = new FileReader(SqlScriptPath);
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
			scriptRunner.runScript(reader);

			// Optional Part...
			List<Table> tableList; // Used to store result of 'SELECT' Query execution

			tableList = scriptRunner.getTableList();
	
			table_data_array = tableList.get(0).getToupleList().toString().replaceAll("\\[\\[","").replaceAll("]]", "");
			conn.close();
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				pw.print(out+e);
			}
			pw.print(table_data_array);
			pw.close();
	}

}
