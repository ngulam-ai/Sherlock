package agency.akcom.mmg.sherlock;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CollectServlet", urlPatterns = { "/collect" })
public class CollectServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.getAttributeNames();
		
		System.out.println("GET collect servlet: ");
		Enumeration<String> attributeNames = req.getAttributeNames();
		while (attributeNames.hasMoreElements())
			System.out.println(attributeNames.nextElement());
		
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements())
			System.out.println(headerNames.nextElement());
			
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().print("GET collect servlet");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		System.out.println("POST collect servlet");
		resp.getWriter().print("POST collect servlet");
	}

}