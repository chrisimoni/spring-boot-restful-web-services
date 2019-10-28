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
import com.chrisreal.ws.shared.dto.UserDto;

public class AmazonSES {
	//This email address must be verified with Amazon SES
	final String FROM = "imonireal@gmail.com";
	
	//The subject for the email;
	final String SUBJECT = "One last step to complete your registration";
	
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
	
	public void verifyEmail(UserDto userDto) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAST27BXA5UOKMIXXK", "YZGjdqFxVXizVYPg0dUoTvqFpu2imkBqb07tLgmY");
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
}
