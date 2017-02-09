package local.toan.core;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.opencsv.CSVReader;
//import au.com.bytecode.CSVReader;

public class PlanResultProfitDataProcess {
/*	public static void main(String[] args) {
		String filename="C:\\Temp\\KTR20160630092730_201606_Achievement1_KTR.csv";
		PlanResultProfitDataProcess rsDataProcess = new PlanResultProfitDataProcess();
		rsDataProcess.ResultDataProcess(filename);
	}
*/
	public void ResultDataProcess(String filename, String datatype) {
		// TODO Auto-generated method stub
		String output_csv = "C:\\Temp\\expense_out_temp.csv";
		//String output_csv = "expense_out_temp.csv";
		CSVReader reader;
		FileWriter writer;
		
		try {
			reader=new CSVReader(new FileReader(filename));
			writer=new FileWriter(output_csv);
			String[] row;
			String org_CD = null;
			String make_DT = null;
			String target_YM  = null;
			String expensecode = null;
			String id_temp = null;
			String id = null;
			String output_row=null;
			String import_result_csv=null;
			
			ResultSet expense_rs=null;
			Class.forName("org.postgresql.Driver");
			Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/planresultprofit","postgres","Ruangoc123");
			String sql="SELECT expensecode FROM expense WHERE expensename=?";
			//PreparedStatement expense_query=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			PreparedStatement expense_query=conn.prepareStatement(sql);
			Statement import_result_stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			
		switch(datatype) {
			//----------Update in to result table
			case "result":
				import_result_csv="COPY result FROM 'C:\\Temp\\expense_out_temp.csv' DELIMITER ',' CSV";
				//phuong thuc reader doc tung truong trong file csv cach nhau boi dau ,
				while((row=reader.readNext())!=null) {
					if(row[0].equals("SSTJ")) {
						org_CD=row[1].substring(0, 8);
						row=reader.readNext();
						make_DT=row[6].substring(10,20);
						row=reader.readNext();
						row=reader.readNext();
						target_YM=row[1].substring(10,17);
						row=reader.readNext();
					}
					if(row[0].equals("SDTL")&&(row[1].equals("2"))){
						//System.out.print(org_CD+","+target_YM+","+make_DT+",\""+row[2]+"\",\""+row[4]+"\"");
						expense_query.setString(1,row[2]);
						expense_rs=expense_query.executeQuery();
						if(expense_rs.next()){
							//expense_rs.absolute(1);
							expensecode = expense_rs.getString(1);
							id_temp = org_CD + make_DT + expensecode;
							id = id_temp.replace("/","");
							output_row = id+","+org_CD+","+target_YM+","+make_DT+","+expensecode+","+row[2]+","+row[8].replace(",", "")+"\n";
							writer.write(output_row);
							//System.out.print(output_row);
						}

					}
				}
				writer.close();
				import_result_stmt.executeUpdate(import_result_csv);
				conn.close();
				break;
			//----------Update into plan table
			case "plan":
				import_result_csv="COPY plan FROM 'C:\\Temp\\expense_out_temp.csv' DELIMITER ',' CSV";
				//phuong thuc reader doc tung truong trong file csv cach nhau boi dau ,
				while((row=reader.readNext())!=null) {
					if(row[0].equals("SSTJ")) {
						org_CD=row[1].substring(0, 8);
						row=reader.readNext();
						make_DT=row[6].substring(10,20);
						row=reader.readNext();
						row=reader.readNext();
						target_YM=row[1].substring(10,17);
						row=reader.readNext();
					}
					if(row[0].equals("SDTL")&&(row[1].equals("2"))){
						//System.out.print(org_CD+","+target_YM+","+make_DT+",\""+row[2]+"\",\""+row[4]+"\"");
						expense_query.setString(1,row[2]);
						expense_rs=expense_query.executeQuery();
						if(expense_rs.next()){
							//expense_rs.absolute(1);
							expensecode = expense_rs.getString(1);
							id_temp = org_CD + target_YM + expensecode;
							id = id_temp.replace("/","");
							output_row = id+","+org_CD+","+target_YM+","+expensecode+","+row[2]+","+row[6].replace(",", "")+"\n";
							writer.write(output_row);
							//System.out.print(output_row);
						}
						

					}
				}
				writer.close();
				import_result_stmt.executeUpdate(import_result_csv);
				conn.close();
				break;
			//----------Update into master plan table
			case "masterplan":
				import_result_csv="COPY masterplan FROM 'C:\\Temp\\expense_out_temp.csv' DELIMITER ',' CSV";
				//phuong thuc reader doc tung truong trong file csv cach nhau boi dau ,
				while((row=reader.readNext())!=null) {
					if(row[0].equals("SSTJ")) {
						org_CD=row[1].substring(0, 8);
						row=reader.readNext();
						make_DT=row[6].substring(10,20);
						row=reader.readNext();
						row=reader.readNext();
						target_YM=row[1].substring(10,17);
						row=reader.readNext();
					}
					if(row[0].equals("SDTL")&&(row[1].equals("2"))){
						//System.out.print(org_CD+","+target_YM+",\""+row[2]+"\",\""+row[4]+"\"");
						expense_query.setString(1,row[2]);
						expense_rs=expense_query.executeQuery();
						if(expense_rs.next()){
							//expense_rs.absolute(1);
							expensecode = expense_rs.getString(1);
							id_temp = org_CD + target_YM + expensecode;
							id = id_temp.replace("/","");
							output_row = id+","+org_CD+","+target_YM+","+expensecode+","+row[2]+","+row[4].replace(",", "")+"\n";
							writer.write(output_row);
							//System.out.print(output_row);
						}
						
					}
				}
				writer.close();
				import_result_stmt.executeUpdate(import_result_csv);
				conn.close();
				break;
		}
		}catch(FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		catch(IOException e) {
			System.err.println(e.getMessage());
		}
		catch(Exception e){System.out.println(e);}
	}
	
}
