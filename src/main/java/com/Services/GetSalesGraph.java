package com.Services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.Models.ClaimsModel;

/**
 * Servlet implementation class GetSalesGraph
 */
public class GetSalesGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSalesGraph() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		String type = request.getParameter("type");
		
		if (type.equals("sales")) {
			
			out.println("<iframe width=\"600\" height=\"400\" src=\"https://live.amcharts.com/zYmFi/embed/\" frameborder=\"0\"></iframe>");
		}

		else if (type.equals("revenue")) {
			
			out.println("<iframe width=\"600\" height=\"400\" src=\"https://live.amcharts.com/mYzFj/embed/\" frameborder=\"0\"></iframe>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
