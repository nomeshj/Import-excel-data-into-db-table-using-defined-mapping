package com.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

/**
 * Servlet implementation class Excel
 */
public class Excel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Excel() {
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
//		   response.setContentType("application/x-download");
//		response.setContentType("application/vnd.ms-excel");
//           response.setHeader("Content-Disposition","attachment; filename=\"" + request.getParameter("load")+".xlsx" + "\"");
	
		System.out.println(request.getParameter("excel"));
		System.out.println(request.getParameter("load"));
		JSONObject data = new JSONObject(request.getParameter("json"));
//		JSONObject json = new JSONObject(request.getParameter("excel"));
		JSONObject json = data.getJSONObject("excel");
		System.out.println("json = "+json);
		 XSSFWorkbook workbook = new XSSFWorkbook();
//	        XSSFSheet sheet = workbook.createSheet(request.getParameter("load"));
	        XSSFSheet sheet = workbook.createSheet(data.getString("load"));
	         
	        int rowCount = 1;
	        CellStyle cellStyle = null;
	        for (int i=1;i<=json.length();i++) {
	            Row row = sheet.createRow(rowCount);
	                  
	            int columnCount;
	            
	            if(i ==1)
	            {
	            	 Font font = workbook.createFont();
	                 font.setBold(true);
	                 cellStyle = workbook.createCellStyle();
	                 cellStyle.setFont(font);
	                 
	            	columnCount = 1;
	            	Cell cell = row.createCell(columnCount);
	                cell.setCellStyle(cellStyle);
	            	cell.setCellValue("View Selected");
	            	
	            	rowCount++;
	            	row = sheet.createRow(rowCount);
	            	
	            	cell = row.createCell(columnCount);
	            	cell.setCellStyle(cellStyle);
	            	cell.setCellValue("loadName");
	            	cell = row.createCell(++columnCount);
	            	cell.setCellValue((data.getString("load")));
	            	
	            	rowCount++;
	            	columnCount = 1;
	            	row = sheet.createRow(rowCount);
	            	
	            	cell = row.createCell(columnCount);
	            	cell.setCellStyle(cellStyle);
	            	cell.setCellValue("inby");
	            	cell = row.createCell(++columnCount);
	            	cell.setCellValue("Nomesh");
	            	
	            	rowCount++;
	            	columnCount = 1;
	            	row = sheet.createRow(rowCount);
	            	
	            	cell = row.createCell(columnCount);
	            	cell.setCellStyle(cellStyle);
	            	cell.setCellValue("indt");
	            	cell = row.createCell(++columnCount);
	            	cell.setCellValue("21/2/23");
	            	
	            	columnCount = 0;
	            	
	            	rowCount++;
	            	rowCount++;
	            	row = sheet.createRow(rowCount);
	            }else {
	            	columnCount = 0;
	            }
	            
	            String val = json.getString(String.valueOf(i));
	            String[] cols = val.split("~");
	            for (String col : cols) {
	                Cell cell = row.createCell(++columnCount);
	                if (col instanceof String) {
	                	if(i ==1)
	                		cell.setCellStyle(cellStyle);
	                	
	                    cell.setCellValue((String) col);
	                }
	            }
	             rowCount++;
	        }
	         
	         
	        try (FileOutputStream outputStream = new FileOutputStream(data.getString("load")+".xlsx")) {
	            workbook.write(outputStream);
	            
	            String filePath = data.getString("load")+".xlsx";
	            File downloadFile = new File(filePath);
	            FileInputStream inStream = new FileInputStream(downloadFile);
	             
	            // if you want to use a relative path to context root:
	            String relativePath = getServletContext().getRealPath("");
	            System.out.println("relativePath = " + relativePath);
	             
	            // obtains ServletContext
	            ServletContext context = getServletContext();
	             
	            // gets MIME type of the file
	            String mimeType = context.getMimeType(filePath);
	            if (mimeType == null) {        
	                // set to binary type if MIME mapping not found
	                mimeType = "application/octet-stream";
	            }
	            System.out.println("MIME type: " + mimeType);
	             
	            // modifies response
	            response.setContentType(mimeType);
	            response.setContentLength((int) downloadFile.length());
	             
	            // forces download
	            String headerKey = "Content-Disposition";
	            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
	            response.setHeader(headerKey, headerValue);
	             
	            // obtains response's output stream
	            OutputStream outStream = response.getOutputStream();
	             
	            byte[] buffer = new byte[4096];
	            int bytesRead = -1;
	             
	            while ((bytesRead = inStream.read(buffer)) != -1) {
	                outStream.write(buffer, 0, bytesRead);
	            }
	             
	            inStream.close();
	            outStream.close();
	               
	        }
	}

}
