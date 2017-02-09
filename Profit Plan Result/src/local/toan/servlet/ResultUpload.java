package local.toan.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import local.toan.core.PlanResultProfitDataProcess;

/**
 * Servlet implementation class ResultUpload
 */
@WebServlet("/ResultUpload")
public class ResultUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultUpload() {
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
		request.getRequestDispatcher("upload.jsp").include(request, response);
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String result_csv_path=request.getParameter("result_csv_path");
		String data_type = request.getParameter("data_type");
		
		PlanResultProfitDataProcess rsDataProcess = new PlanResultProfitDataProcess();
		rsDataProcess.ResultDataProcess(result_csv_path,data_type);
		out.println("Upload Data Complete!");
	}

}
