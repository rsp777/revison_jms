package com.revisionjms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Send
 */
@WebServlet("/Send")
public class Send extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Connection con = null;
		String msg = request.getParameter("msg");
		
		try {
			Context contx = new InitialContext();
			Queue queue = (Queue) contx.lookup("java:/revisionJMS");
			Destination dest = queue;
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) contx.lookup("java:/ConnectionFactory");
			con = qcf.createConnection();
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(dest);
			TextMessage message = session.createTextMessage(msg);
			producer.send(message);
			System.out.println("Sending Message:  "+message.getText());
			out.println("<html><body>");
			out.println("Message Sent Successfully:"+message.getText());
			out.println("</html></body>");
			
		} 
		catch (Exception e) {
			System.err.println("Exception Occured: "+e.toString());
		}
		finally {
			try {
				con.close();
			} catch (JMSException e2) {
				e2.printStackTrace();
			}
		}
	}

}
