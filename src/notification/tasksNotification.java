package notification;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import model.Caregiver;
import model.Scheduledtask;
import model.User;
import DAO.DBHelper;


@Stateless
@LocalBean
@Path("/Notifications")
public class tasksNotification {
	/** The uri info. */
	@Context
	UriInfo uriInfo;
	/** The request. */
	@Context
	Request request;
	
	@Path("user/{user_id}")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response sendMailUser(@PathParam("user_id") int id,String subject,String Text){
		EntityManager em = DBHelper.instance.createEntityManager();
		User u = em.find(User.class, id);
		em.close();
		if(u==null)
			u = new User();
		String uMail=u.getUserEmail().toString();
		final String username = "trento.univ@gmail.com";
		final String password = "trentouniv39";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("trento.univ@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(uMail));
			message.setSubject(subject);
			message.setText(Text
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
			/*System.out.println(e.getMessage());*/
		}
		
		return Response.ok("Mail sent successfully to user with id: "+id , "text/plain").entity("done").build();
		
	}
	
	@Path("cg/{cg_id}")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response sendMailCG(@PathParam("cg_id") int id,String subject,String Text){
		EntityManager em = DBHelper.instance.createEntityManager();
		Caregiver cg = em.find(Caregiver.class, id);
		em.close();
		if(cg==null)
			cg = new Caregiver();
		String cgMail=cg.getCgEmail().toString();
		final String username = "trento.univ@gmail.com";
		final String password = "trentouniv39";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zabsa90@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(cgMail));
			message.setSubject(subject);
			message.setText(Text
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
			/*System.out.println(e.getMessage());*/
		}
		
		return Response.ok("Mail sent successfully to care giver with id: "+id , "text/plain").entity("done").build();
		
	}
	
	@Path("/User")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<User> sendUserReminder(){
		
		   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		   Date date = new Date();
		   System.out.println(dateFormat.format(date));
		 
		EntityManager em = DBHelper.instance.createEntityManager();
		List<User> u = em.createQuery("select u from user u join u.scheduledtask st "
				+ "where st.SchTask_user_checked = 1 and(st.ScheTask_to_time - dateFormat.format(date) < 2")
				.getResultList();
		em.close();
		for(int i=0;i<u.size();i++)
		{
			String email=u.get(i).getUserEmail();
			MailSender.sendMailUser(email);
		}
		return u;
	}
	@Path("/Caregiver")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Caregiver> sendCgReminder(){
		
		   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		   Date date = new Date();
		   System.out.println(dateFormat.format(date));
		 
		EntityManager em = DBHelper.instance.createEntityManager();
		List<Caregiver> cg = em.createQuery("select cg from caregiver cg join cg.scheduledtask st "
				+ "where st.SchTask_user_checked = 1 and(st.ScheTask_to_time - dateFormat.format(date) < 2")
				.getResultList();
		em.close();
		for(int i=0;i<cg.size();i++)
		{
			String email=cg.get(i).getCgEmail();
			MailSender.sendMailUser(email);
		}
		return cg;
	}
}
