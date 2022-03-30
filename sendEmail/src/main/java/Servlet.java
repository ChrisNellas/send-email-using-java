import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String senderEmail;
	private String password;
	private String recipientEmail;
	
	private Session session;
	private Properties properties;
	private Message msg;

    public Servlet() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		senderEmail = ""; //your email
		password = ""; // password of the email
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		recipientEmail = request.getParameter("recipient"); // get the recipient from the form's field
		
		//connect to your email
		session = Session.getInstance(properties, new Authenticator()  {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, password);
			}	
		});
		
		//create a message
		msg = new MimeMessage(session);
	
		// write the email with the given data
		try {
			msg.setFrom(new InternetAddress(senderEmail));
			msg.setRecipient(RecipientType.TO, new InternetAddress(recipientEmail));
			msg.setSubject(request.getParameter("subject"));
			msg.setText(request.getParameter("textarea"));
			Transport.send(msg);//send the email
			response.sendRedirect("form.html"); 
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
