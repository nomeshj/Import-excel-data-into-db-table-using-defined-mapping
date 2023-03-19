package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;

/**
 * Servlet implementation class index
 */
@MultipartConfig
public class index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public index() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static boolean hasNotColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return false;
            }
        }
        return true;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		StringBuffer SQL=new StringBuffer();
		out.write("<script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>");
		out.write("<script type=\"text/javascript\">\r\n"
				+ " function send(e){\r\n"
				+ "        	var name = document.getElementById(\"loadName\").value;\r\n"
				+ "        	document.getElementById(\"load\").value = name;\r\n"
				+ "        	document.excel.submit();\r\n"
				+ "        }"
				+ "        </script>"
				);
	 
		
		if(request.getParameter("Totalrows")!=null)
		{					
			out.write("<script>");
			out.write("if (confirm('File containes "+request.getParameter("Totalrows")+" rows. Do you wish to continue? ') == false) {\r\n"
					+ "				window.location.replace('http://localhost:7676/W4/index');"
					+ "			}");
			out.write("</script>");
		}
		
		out.write("<style>\r\n"
				+ "table, td, th {\r\n"
				+ "  border: 1px solid;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "table {\r\n"
				+ "  width: 100%;\r\n"
				+ "  border-collapse: collapse;\r\n"
				+ "}\r\n"
				+ "</style>\r\n"
				+ "</head>\r\n"
				+ "<body>");
		
		out.write("<style type=\"text/css\">\r\n"
				+ "		.loader-div {\r\n"
				+ "			display: none;\r\n"
				+ "			position: fixed;\r\n"
				+ "			margin: 0px;\r\n"
				+ "			padding: 0px;\r\n"
				+ "			right: 0px;\r\n"
				+ "			top: 0px;\r\n"
				+ "			width: 100%;\r\n"
				+ "			height: 100%;\r\n"
				+ "			background-color: #fff;\r\n"
				+ "			z-index: 30001;\r\n"
				+ "			opacity: 0.8;\r\n"
				+ "		}\r\n"
				+ "		.loader-img {\r\n"
				+ "			position: absolute;\r\n"
				+ "			top: 0;\r\n"
				+ "			bottom: 0;\r\n"
				+ "			left: 0;\r\n"
				+ "			right: 0;\r\n"
				+ "			margin: auto;\r\n"
				+ "		}\r\n"
				+ ""
				+ "#button{\r\n"
				+ "  display:block;\r\n"
				+ "  margin:20px auto;\r\n"
				+ "  padding:10px 30px;\r\n"
				+ "  background-color:#eee;\r\n"
				+ "  border:solid #ccc 1px;\r\n"
				+ "  cursor: pointer;\r\n"
				+ "}\r\n"
				+ "#overlay{	\r\n"
				+ "  position: fixed;\r\n"
				+ "  top: 0;\r\n"
				+ "  z-index: 100;\r\n"
				+ "  width: 100%;\r\n"
				+ "  height:100%;\r\n"
				+ "  display: none;\r\n"
				+ "  background: rgba(0,0,0,0.6);\r\n"
				+ "}\r\n"
				+ ".cv-spinner {\r\n"
				+ "  height: 100%;\r\n"
				+ "  display: flex;\r\n"
				+ "  justify-content: center;\r\n"
				+ "  align-items: center;  \r\n"
				+ "}\r\n"
				+ ".spinner {\r\n"
				+ "  width: 40px;\r\n"
				+ "  height: 40px;\r\n"
				+ "  border: 4px #ddd solid;\r\n"
				+ "  border-top: 4px #2e93e6 solid;\r\n"
				+ "  border-radius: 50%;\r\n"
				+ "  animation: sp-anime 0.8s infinite linear;\r\n"
				+ "}\r\n"
				+ "@keyframes sp-anime {\r\n"
				+ "  100% { \r\n"
				+ "    transform: rotate(360deg); \r\n"
				+ "  }\r\n"
				+ "}\r\n"
				+ ".is-hide{\r\n"
				+ "  display:none;\r\n"
				+ "}"
				+ "	</style>");
		
	
		
	    Set Load = new LinkedHashSet();
	    
	    File f1 = new File("E://Excel//DataLoadMaps.xlsx");
	    Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(f1);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    Sheet sheet = workbook.getSheetAt(0);
	    
	    DataFormatter dataFormatter = new DataFormatter();

	    System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
	    Iterator<Row> rowIterator = sheet.rowIterator();
	    
	    int index = 0;
	    int match = 0;
	    boolean flag = false;
	    
	    Connection con = null;
	    Statement st = null;
	    ResultSet rs = null;
	    JSONObject json = new JSONObject();
	    
	    while (rowIterator.hasNext()) {
	        Row row = rowIterator.next();

	        // Now let's iterate over the columns of the current row
	        Iterator<Cell> cellIterator = row.cellIterator();
			index=0;
			flag = false;
	        while (cellIterator.hasNext()) {
	        	index++;
	            Cell cell = cellIterator.next();
	            String cellValue = dataFormatter.formatCellValue(cell);
	            System.out.print(cellValue + "\t");
	            
	            if(cellValue.equalsIgnoreCase("loadname"))
	            {
	            	match = index;
	            	flag = true;
	            }
	            if(match == index && !flag)
	            {
	            	Load.add(cellValue);
	            }
	        }
	        System.out.println();
	    }
	        System.out.println("Load "+Load.toString());
	        	        
	        out.write("<div style=\"\r\n"
	        		+ "    display: flex;\r\n"
	        		+ "    justify-content: space-around;\r\n"
	        		+ "\">");
	        
	        out.write("<div style=\"\r\n"
	        		+ "    display: flex;\r\n"
	        		+ "    flex-direction: column;\r\n"
	        		+ "\">");
	        
	        out.write("<b>Select Load Mapping</b>\r\n"
	        		+ "        <select name=\"loadName1\" id=\"loadName\">\r\n"
	        		+ "        <option value=\"none\" selected disabled hidden>Select an Option</option>");
	        
	        Iterator<String> i=Load.iterator();  
	        while(i.hasNext())  
	        {          	  
	        	String name = i.next();
	        			if(request.getParameter("load1")!=null)
	        			{	
	        				if(request.getParameter("load1").equalsIgnoreCase(name))
	        				{	        					
	        					out.write("<option value='"+name+"' selected>"+name+"</option>");		        	
	        				}
	        				else {
	        					out.write("<option value='"+name+"' >"+name+"</option>");
	        				}
	        			}
	        			else {
	        				out.write("<option value='"+name+"'>"+name+"</option>");
	        			}
	        }
	        out.write("</select>\r\n"
	        		+ "        </div>");
	        
	        out.write("<div style=\"\r\n"
	        		+ "    display: flex;\r\n"
	        		+ "    flex-direction: column;\r\n"
	        		+ "\">");
	        
	        out.write("<b>Enter Load Date</b>\r\n"
	        		+ "		<input type='date' name='date' id='date' />\r\n"
	        		+ "	</div>");
	        
	        out.write("<div style=\"\r\n"
	        		+ "    display: flex;\r\n"
	        		+ "    flex-direction: column;\r\n"
	        		+ "\">\r\n"
	        		+ "		<form name ='excel' action=\"\" method=\"POST\" enctype=\"multipart/form-data\">\r\n"
	        		+"		<input type='hidden' id='load' name='load1'/>"
	        		+ "		<input type=\"file\" onchange=\"send()\" name=\"file\" value='Import Data from Excel'/>\r\n"
	        		+ "		<input type='submit' name='load' hidden/>"
	        		+ "		</form>\r\n"
	        		+ "		<form method='POST' action='Excel'>"
	        		+ "		<input type=\"submit\" style=\"width: 100%;\"id='export' value='Export View to Excel' disabled/>\r\n"
	        		+"		<input type='hidden' name='json' />"
	        		+"</form>"
	        		+ "<form name ='refresh' action=\"index\" method=\"GET\">"
	        		+ "		<input type=\"submit\" value='Refresh View'style=\"\r\n"
	        		+ "    width: 100%;\r\n"
	        		+ "\" id='refresh'/>\r\n"
	        		+ "<input type='hidden' id='load12' name='load1'/>"
	        		+ "<input type='hidden' id='date12' name='date'/>"
	        		+ "</form>"
	        		+ "	</div>\r\n"
	        		+ "	\r\n"
	        		+ "	<div>\r\n"
	        		+"		<form method='GET' action='\'> "
	        		+ "		<input type=\"submit\" value='Remove view data'/>\r\n"
	        		+ "		</form>"
	        		+ "	</div>\r\n"
	        		+ "    </div>");
	    
	        
	        if(request.getParameter("load1")!=null)
	        {
	         		        	
	        	int viewIndex = 0;
	        	int matchIndex = 0;
	        	
	        	int viewColumnIndex = 0;
	        	
	        	rowIterator = sheet.rowIterator();

	        	
	        	Map<Integer,String> columns = new TreeMap();
	        	Map<Integer,String> fileCol = new TreeMap();
	        	
	        	Map<Integer,String> tableFormat = new TreeMap();
	        	Map tableColumn = new TreeMap();
	        	Map col = new LinkedHashMap();
	        	
	        	
	        	int viewOrderIndex = 0;
	        	int fileColIndex = 0;
	        	int formatIndex = 0;
	        	int proxyIndex = 0;
	        	int tableNameIndex=0;
	        	int tableColumnIndex = 0;
	        	
	        	String tableName ="";
	        	String status="success";
	        	String error="";
	        	java.util.Date startdate1 = new java.util.Date();
	        	long t1 = startdate1.getTime();
	        	java.sql.Date startdate2 = new java.sql.Date(t1);
	        	int loadedRows=0;
	        	int failedRows = 0;
	        	int proxyUsed=0;
	        	
	        	
	        	
	        	
	        	try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        	try {
	        	while (rowIterator.hasNext()) {
	        		
	        		boolean viewOrderError = false;
				      
	    	        Row row = rowIterator.next();

	    	        Iterator<Cell> cellIterator = row.cellIterator();
	    	        boolean flag1= false;
	    	        boolean tableCheck = false;
	    	        boolean columnCheck = false;
	    	        
	    	        viewIndex = 0;
	    	        String fileColName = null;
	    	        int viewOrder = 0;
	    	        while (cellIterator.hasNext()) {
	    	        	viewIndex++;
	    	            Cell cell = cellIterator.next();
	    	            String cellValue = dataFormatter.formatCellValue(cell);
	    	            String format = null;
	    	            
	    	            
	    	            if(cellValue.equalsIgnoreCase(request.getParameter("load1")))
	    	            {
	    	            	tableCheck = true;
	    	            }
	    	            if(cellValue.equalsIgnoreCase("viewColName"))
	    	            {
	    	            	matchIndex=viewIndex;
	    	            	flag1 = true;
	    	            	System.out.println("viewColName");
	    	            }
	    	            if(cellValue.equalsIgnoreCase("viewOrder"))
	    	            {
	    	            	System.out.println("vieworder");
	    	            	viewOrderIndex = viewIndex;
	    	            }
	    	            if(cellValue.equalsIgnoreCase("tableName"))
	    	            {
	    	            	System.out.println("tableName");
	    	            	tableNameIndex = viewIndex;
	    	            }
	    	            if(cellValue.equalsIgnoreCase("tableColumn"))
	    	            {
	    	            	System.out.println("tableColumn");
	    	            	tableColumnIndex = viewIndex;
	    	            }
	    	            if(cellValue.equalsIgnoreCase("fileCol"))
	    	            {
	    	            	System.out.println("fileCol");
	    	            	fileColIndex = viewIndex;
	    	            	System.out.println("filecolIndex = "+fileColIndex);
	    	            }
	    	            if(cellValue.equalsIgnoreCase("tableColFmt"))
	    	            {
	    	            	System.out.println("format");
	    	            	formatIndex = viewIndex;
	    	            	System.out.println("formatIndex = "+formatIndex);
	    	            }
	    	            if(cellValue.equalsIgnoreCase("proxy"))
	    	            {
	    	            	System.out.println("proxy");
	    	            	proxyIndex = viewIndex;
	    	            	System.out.println("proxyIndex = "+proxyIndex);
	    	            }
	    	            if(tableCheck && viewOrderIndex == viewIndex && !flag1 && cellValue.equalsIgnoreCase("NULL"))
	    	            {
	    	            	String val = null;
	    	            	if(cellValue.equalsIgnoreCase("NULL")) {
	    	            		proxyUsed++;
	    	            		val = row.getCell(proxyIndex).toString();
	    	            	}
	    	            	try {
	    	            	viewOrder = Integer.parseInt(val);
	    	            	}catch(Exception e)
	    	            	{	
	    	            		status = "Fail";
	    	            		error = e.getMessage();
	    	            		viewOrderError = true;
	    	            		failedRows++;
	    	            		columnCheck = true;
	    	            	}
	    	            	System.out.println("&&&&&&&&&&&&&&&&&&&&&& "+viewOrderError+"   "+viewOrder);
	    	            }
	    	            if(tableCheck && viewOrderIndex == viewIndex && !flag1 && !columnCheck)
	    	            {
	    	            	loadedRows++;
	    	            	System.out.println("vieworderindex");
	    	            	
	    	            	viewOrder =Integer.parseInt(cellValue);
	    	            	
	    	            }
	    	            if(tableCheck && fileColIndex == viewIndex && !flag1 && !columnCheck)
	    	            {
	    	            	System.out.println("filecolindex");	    	            	
	    	            	fileColName = cellValue;
	    	            }
	    	            if(tableCheck && formatIndex == viewIndex && !flag1 )
	    	            {
	    	            	System.out.println("formatindex = "+cellValue);	
	    	            		
	    	            	format = cellValue;
	    	            	if(cellValue.equalsIgnoreCase("NULL") && !viewOrderError)
	    	            		format = row.getCell(proxyIndex).toString();
	    	            	tableFormat.put(viewOrder, format);
	    	            }
	    	            if(tableCheck && tableNameIndex == viewIndex && !flag1 && !columnCheck)
	    	            {
	    	            	System.out.println("tabelNameindex = "+cellValue);	    	            	
	    	            	tableName = cellValue;
	    	            }
	    	            if(tableCheck && tableColumnIndex == viewIndex && !flag1)
	    	            {

	    	            	
	    	            	if(!viewOrderError)
	    	            	{
	    	            		System.out.println("tabelColumnindex = "+dataFormatter.formatCellValue(cell)+"  "+tableColumnIndex);	
	    	            		System.out.println("*******************************");
	    	            		System.out.println(viewOrder+"  "+cellValue+"  "+viewOrderError );
	    	            		System.out.println("*******************************");
	    	            		tableColumn.put(viewOrder,cellValue);
	    	            	}
	    	            }
	    	            if(matchIndex == viewIndex && !flag1 && tableCheck && !columnCheck) {
	    	            	System.out.println("viewColumnName");
	    	            	System.out.println("map ="+viewOrder+" "+cellValue);
	    	            	
	    	            	if(cellValue.equalsIgnoreCase("NULL") && !viewOrderError)
	    	            	{
	    	            		columns.put(viewOrder,row.getCell(proxyIndex).toString());
	    	            	}else {
	    	            		columns.put(viewOrder,cellValue);	    	            		
	    	            	}
	    	            	if(cellValue.equalsIgnoreCase("NULL") && !viewOrderError)
	    	            	{
	    	            		fileCol.put(viewOrder,row.getCell(proxyIndex).toString());
	    	            	}else {
	    	            		fileCol.put(viewOrder,fileColName);	    	            		
	    	            	}
	
	    	            }

	    	           System.out.println("check"+tableCheck);
	    	           
	    	        }
	    	        System.out.println();
	    	    }
	        	}catch(Exception e)
	        	{
	        		status = "fail";
	        		error = e.toString();
	        	}
	        	
	        	System.out.println("col = "+col);
	        	System.out.println("fileCol = "+fileCol);
	        	System.out.println("tableColumn = "+tableColumn);
	        	java.util.Date enddate1 = new java.util.Date();
	        	long t2 = enddate1.getTime();
	        	java.sql.Date enddate2 = new java.sql.Date(t2);
	        	
	        	// dataloadlogs
	        	try {
	        	DatabaseMetaData dbm = con.getMetaData();
				// check if  table is there
				ResultSet tables1 = dbm.getTables(null, null, "DATALOADLOG", null);    
				if(!tables1.next())
				{
					st = con.createStatement();
					System.out.println("creating LOG table *************************");
					st.executeUpdate("CREATE TABLE DATALOADLOG (LOADNAME VARCHAR2(255),STATUS VARCHAR2(255),STARTDT DATE,ENDDT DATE,LOADEDROWS NUMBER,FAILEDROW NUMBER,PROXYUSEDCNT NUMBER,ERR VARCHAR2(255))");
				
				}
				Format f12 = new SimpleDateFormat("dd-MMMM-yyyy");
				String startDate = f12.format(startdate1);
				String endDate = f12.format(enddate1);
			      System.out.println("Current Date = "+startDate);
			      System.out.println("*****************************date="+startDate);
			      st = con.createStatement();
			      String query = "INSERT INTO DATALOADLOG VALUES ('"+request.getParameter("load1")+"','"+status+"',TO_DATE('"+startDate+"'),TO_DATE('"+endDate+"'),"+loadedRows+","+failedRows+","+proxyUsed+",'"+error+"')";
			      System.out.println(query);
				st.execute(query);
	        	}catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
				//=====
	        	
	        	String table = tableName;
	        	System.out.println("tableName = "+tableName);
	        	
	        	if(request.getParameter("file")!=null)
	        	{
	        	System.out.println(columns.toString());
	             	
	        	
	        	StringJoiner cols = new StringJoiner(",","(",")");
	        	
	        	StringBuffer sql = new StringBuffer("CREATE TABLE "+table+" ");
	        	cols.add("loadName VARCHAR2(200)");
	        	cols.add("inBy VARCHAR2(200)");
	        	cols.add("inDt Date");
	        	cols.add("ord VARCHAR2(200)");
	        	try {
	        		
	        	System.out.println("tableColumn = "+tableColumn);
	        	System.out.println("");
				for(int j =1;j<=tableColumn.size();j++) {
					String column = (String) tableColumn.get(j);

					if(column.contains("s"))
						cols.add(" "+column+" varchar2(255)");
					if(column.contains("d"))
						cols.add(" "+column+" Date");
					if(column.contains("n"))
						cols.add(" "+column+" Number");
					
				}
				sql.append(cols.toString());
				System.out.println("$$$$$$$ Dataloaded = "+sql);

	        	}catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
			
	        	boolean createCheck = false;
	        	try {
					
					st = con.createStatement();
					
					DatabaseMetaData dbm = con.getMetaData();
					// check if  table is there
					ResultSet tables = dbm.getTables(null, null, table.toUpperCase(), null);    
					if(!tables.next())
					{
						System.out.println("creating table *************************");
						st.executeUpdate(sql.toString());
						createCheck = true;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	             	
	        	// data for table
	        	
	        	File f2 = new File(System.getProperty("user.dir")+"\\" + request.getParameter("file"));
	    	    workbook = null;
	    		try {
	    			workbook = WorkbookFactory.create(f2);
	    		} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	    
	    	    sheet = workbook.getSheetAt(0);
	    	    
	    	    dataFormatter = new DataFormatter();
	    	    
	    	    
	    	   rowIterator = sheet.rowIterator();
	    	    
	    	   int rowIndex=0;
	    	   
	    	
	    	    while (rowIterator.hasNext()) {
	    	    	rowIndex++;
	    	        Row row = rowIterator.next();

	    	        // Now let's iterate over the columns of the current row
	    	       
	    	        	if(rowIndex == 1)
	    	        	{
	    	        		for(Map.Entry m:fileCol.entrySet()){
	    	        			int temp=0;
	    	        			 Iterator<Cell> cellIterator = row.cellIterator();
	    	 	    			
	    	 	    	        while (cellIterator.hasNext()) {
	    	 	    	        	temp++;
	    	 	    	        	 Cell cell = cellIterator.next();
	    	 	    	        	 
	    	 	    		          String cellValue = dataFormatter.formatCellValue(cell);
	    	        		       if(cellValue.equalsIgnoreCase((String)m.getValue()))
	    	        		       {
	    	        		    	   System.out.println(m.getKey()+"  "+m.getValue()+"  "+cellValue+"  "+temp);
	    	        		    	   System.out.println("here");
	    	        		    	   col.put(m.getKey(), temp);
	    	        		       }
	    	        		 }   
	    	        	}
	    	        }
					else {
						boolean dataCheck = false;
						
						
						
						
						if(!createCheck)
						{
						
							StringJoiner vals = new StringJoiner(" AND ");
						StringBuffer selectTable = new StringBuffer();
						
						selectTable.append("select *  FROM "+table+" where ");
						
						System.out.println("________"+col);
						System.out.println("________"+tableColumn);
						for(int j=1;j<=tableColumn.size();j++)
						{
							ResultSet rs1 = null;
							try {
								
								
							DatabaseMetaData md = con.getMetaData();
							 rs1 = st.executeQuery("select * from "+table);
							 if (hasNotColumn(rs1,(String)tableColumn.get(j))) {
								 String dataType = null;
								 
								 if(tableColumn.get(j).toString().contains("s"))
									 dataType = "VARCHAR2(255)";
								 if(tableColumn.get(j).toString().contains("d"))
									 dataType = "DATE";
								 if(tableColumn.get(j).toString().contains("n"))
									 dataType = "NUMBER";
								 
							      st.execute("ALTER TABLE "+table+" ADD "+tableColumn.get(j)+" "+dataType+"");
							    }
							}catch(Exception e)
							{
								System.out.println(e.getMessage());
								System.err.println(e.getLocalizedMessage());
							}finally {
								try {
									rs1.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							System.out.println("j="+col.get(j));
							System.out.println(row.getCell((int)col.get(j)));
							String val = row.getCell((int)col.get(j)-1).toString();
							System.out.println(tableFormat.get(j));
							String column = tableFormat.get(j).toString(); 
							
							
							if(column.equalsIgnoreCase("VARCHAR2(255)"))
							{
								val = "'"+val+"'";
							}
							if(column.equalsIgnoreCase("DATE"))
							{
								val = "TO_DATE('"+val+"')";
							}
							
							
							vals.add(tableColumn.get(j)+"="+val);
					}
						selectTable.append(vals.toString());
						System.out.println("+++++++++++++++++++++++++");
						System.out.println("selectTable = "+selectTable);
						
						try {
							rs = st.executeQuery(selectTable.toString());
							if(rs.next())
							{
								System.out.println("bypass");
								dataCheck = true;
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}
						
						if(!dataCheck || createCheck)
						{
						StringBuffer insertTable = new StringBuffer();
				    	   StringJoiner insertCol = new StringJoiner(",","(",")");
				    	   StringJoiner insertVal = new StringJoiner(",","(",")");
				    	   
				    	   insertTable.append("INSERT INTO "+table+" ");
				    	   
				    	   insertCol.add("LOADNAME");
				    	   insertCol.add("INBY");
				    	   insertCol.add("INDT");
				    	   insertCol.add("ORD");
				    	   
				    	   
				    	   for(int p=1;p<=tableColumn.size();p++)
				    	   {
				    		   insertCol.add((CharSequence) tableColumn.get(p));

				    	   }				    	   

				    	   
				    	   
				    	   insertTable.append(insertCol.toString());
				    	   insertTable.append("values ");
				    	   
				    	   insertVal.add("'"+request.getParameter("load1")+"'");
				    	   insertVal.add("'Nomesh'");
				    	   

							java.util.Date date = new java.util.Date();
						      long t = date.getTime();
						      java.sql.Date sqlDate = new java.sql.Date(t);
						     
						      Format f = new SimpleDateFormat("dd/MMMM/yyyy");
						      String strDate = f.format(new java.util.Date());
						      System.out.println("Current Date = "+strDate);
						      System.out.println("*****************************date="+strDate);

							
							insertVal.add("TO_DATE('"+strDate+"')");
							insertVal.add("'"+(rowIndex-1)+"'");
							

						System.out.println("tableFormat = "+tableFormat);
						System.out.println(col.toString());
						for(int j=1;j<=col.size();j++)
						{
							System.out.println("j="+col.get(j));
							System.out.println(row.getCell((int)col.get(j)));
							String val = row.getCell((int)col.get(j)-1).toString();
							System.out.println(tableFormat.get(j));
							String column = tableFormat.get(j).toString(); 
							if(column.equalsIgnoreCase("VARCHAR2(255)"))
							{
								val = "'"+val+"'";
							}
							if(column.equalsIgnoreCase("DATE"))
							{
								val = "TO_DATE('"+val+"')";
							}
							
							
							insertVal.add(val);

							
						}
						insertTable.append(insertVal.toString());
						System.out.println("insert = "+insertTable);
						try {
							st.execute(insertTable.toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
	    	        
	    	        	System.out.println("sa="+row.getCell(0));
	    	    }
	        	
	        	}// end if !file
	        	System.out.println("date = "+request.getParameter("date"));
	        	if(request.getParameter("file")!=null || !request.getParameter("date").equals(""))
	        	{
	    	    try {
	    	    	
	    	    	StringJoiner cols = new StringJoiner(",");
					for (int k = 1; k <= tableColumn.size(); k++) {
						cols.add((CharSequence) tableColumn.get(k)+" AS \""+columns.get(k)+"\"");
					}
	    	    	
	    	    	System.out.println("Tabel = "+table);
	    	    	st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    	    	SQL.append("select "+cols.toString()+" from "+table.toUpperCase()+" where loadname = '"+request.getParameter("load1")+"'");

	    	    		if(request.getParameter("date")!=null)
	    	    	{
	    	    		 String str=request.getParameter("date");  	    	    			
	    	    			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd"); 
	    	    			java.util.Date date;
							try {
								date = dt.parse(str);
								SimpleDateFormat dt1 = new SimpleDateFormat("dd-mm-yy");
								String s = dt1.format(date);
								System.out.println(dt1.format(date));
								SQL.append(" AND INDT = '"+s+"'");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
	    	    	}
	    	    	System.out.println("SQL = "+SQL);
	    	    	
					rs = st.executeQuery(SQL.toString());
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					System.out.println("columncount = "+columnCount);
					int count = 1;
					
					StringBuffer val=new StringBuffer();
		        	String key = "key";
					
					
					
					json.put(String.valueOf(count), val);
					
					System.out.println(count+"==="+val);
					
					while (rs.next()) {
						val = new StringBuffer();
						if(count == 1)
						{
							out.write("<table id='viewTable'>");

							out.write("<tr>");
										
							for (int k = 1; k <= columns.size(); k++) {
								val.append(columns.get(k)+"~");
								out.write("<th>" + columns.get(k) + "</th>");

							}
							out.write("</tr>");
						}else {
						
					
						System.out.println("tableColun = "+tableColumn);
						out.write("<tr name='tabrow'>");
						for(int z=1;z<=tableColumn.size();z++) {
							val.append(rs.getString(z)+"~");
							out.write("<td>" + rs.getString(z) + "</td>");
						}
							

						System.out.println(count+"==="+val);
						out.write("</tr>");
						}
						json.put(String.valueOf(count), val);
						if(rs.isLast())
						{							
							out.write("</table>");
							out.write("<a name ='sql' href='#'>Copy SQL Statement</a>");
						}
						count++;
					}

					
		
					System.out.println("===========");
					System.out.println(json.toString());
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if (con != null) {
						try {
							con.close();
						} catch (Exception e) {
						}
					}
					if (st != null) {
						try {
							st.close();
						} catch (Exception e) {
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (Exception e) {
						}
					}
				}
	        	}
	    	    
	    	    System.out.println("col "+col.toString());
	    	    System.out.println("filecol "+fileCol.toString());
	    	    System.out.println("format "+tableFormat.toString());

	        	
	        	
	        	out.write("</table>");
	        	out.write("<script>");
	        	out.write("$(document).ready(function() { ");
	        	if(request.getParameter("load1")!=null) {
	        	out.write("$('input[name=\"json\"]').val('{\"excel\":"+json+",\"load\":\""+request.getParameter("load1")+"\"}');");
	        	}
	        	
	        	System.out.println("date1 = "+request.getParameter("date"));
	        	if(request.getParameter("date")!=null) {
	        	if(!request.getParameter("date").equals("")) {
	        		out.write("$('input[name=\"date\"').val('"+request.getParameter("date")+"');");
	        	}}
	        	
	        	String showSQL= SQL.toString().replaceAll("'", "\\\\'");
	        	System.out.println("alert = "+showSQL);
				out.write("	$('a[name=\"sql\"]').click(function(event) {"
				+ "	alert('"+showSQL+";');"
				+ "})})");
	        	out.write("</script>");
	        	

	        	
	        	out.write("<script>");
	        	out.write("function Spinner(){\r\n"
	        			+ "	Spinner.element=document.createElementNS('http://www.w3.org/2000/svg', 'svg');\r\n"
	        			+ "	let c=document.createElementNS('http://www.w3.org/2000/svg', 'circle');\r\n"
	        			+ "	Spinner.element.setAttribute('width','100');\r\n"
	        			+ "	Spinner.element.setAttribute('height','100');\r\n"
	        			+ "	c.setAttribute('viewBox','0 0 100 100');\r\n"
	        			+ "	c.setAttribute('cx','50');\r\n"
	        			+ "	c.setAttribute('cy','50');\r\n"
	        			+ "	c.setAttribute('r','42');\r\n"
	        			+ "	c.setAttribute('stroke-width','16');\r\n"
	        			+ "	c.setAttribute('stroke','#2196f3');\r\n"
	        			+ "	c.setAttribute('fill','transparent');\r\n"
	        			+ "	Spinner.element.appendChild(c);\r\n"
	        			+ "	Spinner.element.style.cssText='position:absolute;left:calc(50% - 50px);top:calc(50% - 50px)';\r\n"
	        			+ "	document.body.appendChild(Spinner.element)\r\n"
	        			+ "}\r\n"
	        			+ "Spinner.id=null;\r\n"
	        			+ "Spinner.element=null;\r\n"
	        			+ "Spinner.show=function(){\r\n"
	        			+ "	const c=264,m=15;\r\n"
	        			+ "	Spinner.element.style.display='block';\r\n"
	        			+ "	move1();\r\n"
	        			+ "	function move1(){\r\n"
	        			+ "		let i=0,o=0;\r\n"
	        			+ "		move();\r\n"
	        			+ "		function move(){\r\n"
	        			+ "			if(i==c)move2();\r\n"
	        			+ "			else{\r\n"
	        			+ "				i+=4;o+=8;\r\n"
	        			+ "				Spinner.element.setAttribute('stroke-dasharray',i+' '+(c-i));\r\n"
	        			+ "				Spinner.element.setAttribute('stroke-dashoffset',o)\r\n"
	        			+ "				Spinner.id=setTimeout(move,m)\r\n"
	        			+ "			}\r\n"
	        			+ "		}\r\n"
	        			+ "	}\r\n"
	        			+ "	function move2(){\r\n"
	        			+ "		let i=c,o=c*2;\r\n"
	        			+ "		move();\r\n"
	        			+ "		function move(){\r\n"
	        			+ "			if(i==0)move1();\r\n"
	        			+ "			else{\r\n"
	        			+ "				i-=4;o+=4;\r\n"
	        			+ "				Spinner.element.setAttribute('stroke-dasharray',i+' '+(c-i));\r\n"
	        			+ "				Spinner.element.setAttribute('stroke-dashoffset',o)\r\n"
	        			+ "				Spinner.id=setTimeout(move,m)\r\n"
	        			+ "			}\r\n"
	        			+ "		}\r\n"
	        			+ "	}\r\n"
	        			+ "};\r\n"
	        			+ "Spinner.hide=function(){\r\n"
	        			+ "	Spinner.element.style.display='none';\r\n"
	        			+ "	if(Spinner.id){\r\n"
	        			+ "		clearTimeout(Spinner.id);\r\n"
	        			+ "		Spinner.id=null\r\n"
	        			+ "	}\r\n"
	        			+ "	Spinner.element.setAttribute('stroke-dasharray','0 264');\r\n"
	        			+ "	Spinner.element.setAttribute('stroke-dashoffset','0')\r\n"
	        			+ "};\r\n"
	        			+ "");
	        	out.write("</script>");
	        	out.write("<script>");
	        	out.write("Spinner();"
	        			+ "Spinner.show();");
	        	out.write("window.addEventListener(\"load\",function(){"
	        			+ "if($('input[name=\"date\"]').val() != '' || $('#viewTable tr').length != 0){"
	        			+ "document.getElementById(\"export\").disabled = false;}"
	        			+ "Spinner.hide();"
	        			+ "})");

	        	out.write("</script>");
	        	
	        	out.write("<script>");
	        	
	        	
	        	out.write("$(document).ready(function() {");
	        				if(request.getParameter("date")!=null)
	        			{
	        				out.write("$('#date').val('"+request.getParameter("date")+"');");
	        			}
	        		 
	        			out.write("$('#viewTable').length; "

						+ "$('table').ready(function(event) {"
						+ "if($('#viewTable tr').length == 10)"
						+ "{"
						+ "	$('#loader').text(\"Load Completed\");"
						+ "}"
						+ "})})");
			        	out.write("</script>");
			        	
			        	out.write("<script  type=\"text/javascript\">"
			        			+ "function fn(){"
			        			+ "$(document.table).ready(function(){"
			        			+ "if($('table tr').length>100)"
			        			+ "{"
			        			+ "window.location = location.href;"
			        			+ "}"
			        			+ "});"
			        			+ "}"
			        			+ "setInterval(\"fn();\",10000);"
			        			+ ""
			        			+ "function my_fun(){"
			        			+ "window.location = location.href;"
			        			+ "}"
			        			+ "</script>");
			        	

			        	
			        	
	        	System.out.println("Done!");
	        	
	        	try {
	        		if(rs!=null)
	        			rs.close();
	        		if(st!=null)
	        			st.close();
	        		if(con!=null)
	        			con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }

	    	out.write("<script  type=\"text/javascript\">");
	       	out.write("$(\"#refresh\").on(\"click\", function(e) {\r\n"
	    			+ "    document.getElementById('load12').value = $('#loadName').val();"
	    			+ "    document.getElementById('date12').value = $('#date').val();"
	    			+ "});");
	    	out.write("</script>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Part filePart = request.getPart("file");
	    String fileName = filePart.getSubmittedFileName();
	    fileName = fileName.replace("E:", "");
	    System.out.println(fileName);
	    for (Part part : request.getParts()) {
	      part.write(System.getProperty("user.dir")+"\\" + fileName);
	    }
	   
	    File f1 = new File(System.getProperty("user.dir")+"\\" + fileName);
	    try {
			Workbook workbook = WorkbookFactory.create(f1);
			Sheet sheet = workbook.getSheetAt(0);
			System.out.println(sheet.getLastRowNum());
			int rows = sheet.getLastRowNum();
			System.out.println("rows = "+rows);
			System.out.println("name="+request.getParameter("load1"));
			 response.sendRedirect("index?Totalrows="+(rows-1)+"&load1="+request.getParameter("load1")+"&file="+fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
