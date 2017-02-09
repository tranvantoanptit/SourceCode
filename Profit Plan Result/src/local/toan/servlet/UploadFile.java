package local.toan.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

import local.toan.core.PlanResultProfitDataProcess;


/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*response.getWriter().append("Served at: ").append(request.getContextPath());*/
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
		File file ;
		String filePath = "C:/Temp/UploadData/";
		String fileName_final;
		String result_csv_path="";
		String inputName = ""; 
		String data_type="";

		// Verify the content type
		String contentType = request.getContentType();
		if ((contentType.indexOf("multipart/form-data") >= 0)) {

		   DiskFileItemFactory factory = new DiskFileItemFactory();
		   // maximum size that will be stored in memory
		   //factory.setSizeThreshold(maxMemSize);
		   // Location to save data that is larger than maxMemSize.
		   //factory.setRepository(new File("c:\\Temp"));

		   // Create a new file upload handler
		   ServletFileUpload upload = new ServletFileUpload(factory);
		   // maximum file size to be uploaded.
		   //upload.setSizeMax( maxFileSize );
		   try{ 
		      // Parse the request to get file items.
		      List<?> fileItems = upload.parseRequest(request);

		      // Process the uploaded file items
		      Iterator<?> i = fileItems.iterator();

		      while ( i.hasNext () ) 
		      {
		         FileItem fi = (FileItem)i.next();
		         if ( !fi.isFormField () )	
		         {
		         fi.getFieldName();
		         String fileName = fi.getName();
		         fi.isInMemory();
		         fi.getSize();
		         // Write the file
		            if( fileName.lastIndexOf("\\") >= 0 ){
		            	fileName_final = fileName.substring( fileName.lastIndexOf("\\"));
		               file = new File( filePath + fileName_final) ;
		            }else{
		               fileName_final = fileName.substring( fileName.lastIndexOf("\\"))+1;
		               file = new File( filePath + fileName_final) ;
		            }
		         fi.write( file ) ;
		        
		        result_csv_path = filePath+file.getName();
		        /*result_csv_path = result_csv_path.replaceAll("/", "\\\\");*/
	         		         
		         out.print("<html><head>"
		         		+ "<script>alert(\"Upload complete " + result_csv_path + "\");</script>"
		         		+ "</head><body></body></html>");
		         }
		         else
		         {
		        	 inputName = (String)fi.getFieldName();
		        	 if(inputName.equalsIgnoreCase("data_type"))
		        	 {
		        		 data_type = (String)fi.getString();
		        	 }
		        	 
		         }
			      PlanResultProfitDataProcess rsDataProcess = new PlanResultProfitDataProcess();
			      rsDataProcess.ResultDataProcess(result_csv_path,data_type);
		      }
		      
		   }catch(Exception ex) {
			   out.print("<html><head>"
		         		+ "<script>alert(\"No File Uploaded\");</script>"
		         		+ "</head><body></body></html>");
		   }
		}else{
			out.print("<html><head>"
	         		+ "<script>alert(\"No File Uploaded\");</script>"
	         		+ "</head><body></body></html>");
		}
	}

}
