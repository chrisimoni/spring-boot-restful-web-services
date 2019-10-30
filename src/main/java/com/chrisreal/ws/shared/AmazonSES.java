package com.chrisreal.ws.shared;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.chrisreal.ws.shared.dto.UserDto;

public class AmazonSES {
	//This email address must be verified with Amazon SES
	final String FROM = "imonireal@gmail.com";
	
	//The subject for the email;
	final String SUBJECT = "One last step to complete your registration";
	
	final String PASSWORD_RESET_SUBJECT = "Password reset request";
	
	//HTML body for the email
	final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>Thank you for registering with our app. To complete your registraton process and be able to log in, "
			+ " click on the following link:"
			+ "<a href='http://ec2-34-203-247-65.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue'>"
			+ "Final step to complete your registration</a><br/><br/>"
			+ "Thank you!</p>";
	
	//The email body for recipients with non-HTML email client
	final String TEXTBODY = "Please verify your email address."
			+ "Thank you for registering with our app. To complete your registraton process and be able to log in, "
			+ " Open the following URL in your browser:"
			+ "http://ec2-34-203-247-65.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue"
			+ "Thank you!";
	
	final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>"
			+ "<p>Hi, $firstName!</p> "
			+ "<p>Someone has requested to reset your password. If you are not the one please ignore. "
			+ " Otherwise please click on the following link to set a new password:"
			+ "<a href='http://ec2-34-203-247-65.compute-1.amazonaws.com:8080/verification-service/password-reset.html?token=$tokenValue'>"
			+ "Click this link to reset your password</a><br/><br/>"
			+ "Thank you!</p>";
	
	final String PASSWORD_RESET_TEXTBODY = "A request to reset your password "
			+ "Hi, $firstName! "
			+ "Someone has requested to reset your password. If you are not the one please ignore. "
			+ " Otherwise please open the following link to set a new password: "
			+ "http://ec2-34-203-247-65.compute-1.amazonaws.com:8080/verification-service/password-reset.html?token=$tokenValue "
			+ "Thank you!";
	
	public void verifyEmail(UserDto userDto) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("enter your id", "enter your secret key");
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();
		
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		
		client.sendEmail(request);
		
		System.out.println("Email sent");
	}

	
	public boolean sendPasswordResetRequest(String firstName, String email, String token) {
		boolean returnValue = false;
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("enter your id", "enter your secret key");
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.US_EAST_1)
				.build();
		
		String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
		htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
		
		String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
		textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
		
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(
						new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body()
								.withHtml(new Content()
										.withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content()
										.withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content()
								.withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
				.withSource(FROM);
		
		
		SendEmailResult result = client.sendEmail(request);
		
		if(result !=null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
			returnValue = true;
		}
		
		return returnValue;
	}

}
