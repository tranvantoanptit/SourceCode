package local.toan.servlet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * Servlet implementation class upload303
 */
@WebServlet("/upload")
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	File file ;
    public upload() {
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
		request.getRequestDispatcher("latestprice.jsp").include(request, response);
		response.setContentType("text/html");

		PrintWriter out=response.getWriter();
		ServletContext servletContext = getServletContext();
        String folderPath = servletContext.getRealPath("/resources/UploadData/");

		String fileName_final;
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
		               file = new File( folderPath + fileName_final) ;
		            }else{
		               fileName_final = fileName.substring( fileName.lastIndexOf("\\"))+1;
		               file = new File( folderPath + fileName_final) ;
		            }
		         fi.write( file ) ;
		        
	         		         
		         out.print("<html><head>"
		         		+ "<script>alert(\"Upload complete " + file.getName() + "\");</script>"
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
				 
		      }
		      
		         Class.forName("org.postgresql.Driver");
				 Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
				 Statement insertdatatotable_stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				 String insert_sql = null;
				 
				 CopyManager copyManager = new CopyManager((BaseConnection) conn);  
				 FileReader fileReader = null;
				  		            
				 switch(data_type) {
				 case "PKTF403":
					 insertdatatotable_stmt.executeUpdate("TRUNCATE pktf403;");
					 
					 fileReader = new FileReader(folderPath+file.getName());
					 
					 insert_sql = "COPY pktf403(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,"
					 		+ "f18,f19,f20,f21,f22,f23,f24,f25,f26,f27,f28,f29,f30,f31,f32,f33,f34,f35,f36,f37,"
					 		+ "f38,f39,f40,f41,f42,f43,f44,f45,f46,f47,f48,f49,f50,f51,f52,f53,f54) "
					 		+ "FROM STDIN (FORMAT 'text', DELIMITER E'\t');";
					 break;
				 case "PKTF602":
					 insertdatatotable_stmt.executeUpdate("TRUNCATE pktf602;");
					 
					 fileReader = new FileReader(folderPath+file.getName());
					 insert_sql = "COPY pktf602(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,"
					 		+ "f19,f20,f21,f22,f23,f24,f25,f26,f27,f28,f29,f30,f31,f32,f33,f34,f35,f36,f37,f38,"
					 		+ "f39,f40,f41) FROM STDIN (FORMAT 'text', DELIMITER E'\t');";
					 break;
				 }
				 
				 copyManager.copyIn(insert_sql,fileReader);
				 conn.close();
		      
		   }catch(Exception ex) {
			   out.print("<html><head>"
		         		+ "<script>alert(\"No File Uploaded - " +ex+" \");</script>"
		         		+ "</head><body></body></html>");
		   }
		}else{
			out.print("<html><head>"
	         		+ "<script>alert(\"No File Uploaded\");</script>"
	         		+ "</head><body></body></html>");
		}
	}

}
