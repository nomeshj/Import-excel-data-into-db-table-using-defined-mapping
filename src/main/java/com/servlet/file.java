package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Servlet implementation class file
 */
@MultipartConfig
public class file extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public file() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("name="+request.getParameter("load1"));
//	    RequestDispatcher rd=request.getRequestDispatcher("home?row=4");   
//	    
//	  rd.forward(request, response);
	    response.sendRedirect("home?row=4");

	    response.getWriter().print("The file uploaded sucessfully.");
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
