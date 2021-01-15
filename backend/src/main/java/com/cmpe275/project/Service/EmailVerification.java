package com.cmpe275.project.Service;

import java.io.IOException;

import com.cmpe275.project.Config.Config;
import com.sendgrid.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailVerification {

	@Bean
	public EmailVerification mailService() {
		return new EmailVerification();
	}
	
	public boolean sendEmail(String userToken, String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home SignUp Email verification";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Click on this link to validate your email: "+Config.url+"/activateLogin?userToken="+userToken+"&userEmail="+userEmail);
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean userBookingConfirmation(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking Confirmation";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Thank you for booking with us.....have a nice stay !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean ownerBookingConfirmation(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking Confirmation";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "You have a new booking in your property !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean addProperty(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Property added";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Congracts......new property has been added!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean deleteProperty(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Property Deleted";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Propert has been deleted!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean updateProperty(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Property Updated";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Congracts.....Propert has been Updated!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean paymentAdded(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Payment added";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Congracts.....New Payment Method added!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean userBookingCancelGuest(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking cancellation";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "oops......you have cancelled your booking.....you will be refunded/charged if applicable!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean userBookingCancelHost(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking cancellation";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "oops....guest have cancelled a booking in your property....you will be refunded if applicable !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean hostBookingCancelGuest(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking cancellation";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "oops......Host have cancelled your booking.....you will be refunded if applicable!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean hostBookingCancelHost(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Booking cancellation";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "oops....you have cancelled a booking for a guest in your property....you will be charged/refunded if applicable !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean checkinGuest(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Guest Checkin";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "olaaaaa....you have checked in into your room!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean checkinHost(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Guest Checkin";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Guest have checked in into his room !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean checkoutGuest(String userEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Guest Checkout";
	    Email to = new Email(userEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "olaaaaa....you have checked out from your room!!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
	
	public boolean checkoutHost(String ownerEmail) {
	    Email from = new Email("openhome@gmail.com");
	    String subject = "Open Home Guest Check out";
	    Email to = new Email(ownerEmail);
	    //Content content = new Content("text/plain", "Please click on this link: " +Config.url+"/activateLogin?accessToken="+accessToken+"&email="+email);
	    Content content = new Content("text/plain", "Guest have checked out from his room !!!!");
	    //Content content = new Content("text/plain", "and easy to do anywhere, even with Java");		
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	      return false;
	    }
	    return true;
	  }
}
