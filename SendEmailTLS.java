
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SendEmailTLS {

    public static void main(String[] args) throws IOException, FileNotFoundException, UnsupportedEncodingException {
    	int MAX_EMAIL_PER_USER = 140;
    	String subject = "[SoICT 2019] CFP || Final Submission Deadline: 14 August 2019 || Hanoi - Halong Bay, Vietnam, 5-6 December 2019";
    	//Read the senders.txt file
    	String[] username = new String[10];
    	String[] password = new String[10];
    	int senderNumber=0; 
    	int sentEmailNumber = 0;
    	
    	FileWriter fw = new FileWriter("log.txt", true);
    	BufferedWriter bw = new BufferedWriter(fw);
    	PrintWriter logWriter = new PrintWriter(bw);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	    	
		try {
			BufferedReader senderReader = new BufferedReader(new FileReader(args[0]));
			String line = "abc";
			int count=0;
			while (line != null) {
				line = senderReader.readLine();
				if (count % 2 == 0)
					username[count/2] = line;
				else {
					password[count/2] = line;
					senderNumber++;
				}
				count++;
			}
			senderReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		System.out.println("The number of sender: " + senderNumber);
		
		
		/*for(int k=0;k<user_number;k++) {
			System.out.println(username[k] + "  " + password[k]);
		}*/
		
        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
      //Read the content of the email from the content.txt file
        String emailContent = "";
		try {
			BufferedReader contentReader = new BufferedReader(new FileReader(args[2]));
			String contentLine = contentReader.readLine();
			while (contentLine != null) {
				emailContent = emailContent + "\n" + contentLine;
				contentLine = contentReader.readLine();
			}
			contentReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        //Read receiver file
        try {
        	BufferedReader receiverReader = new BufferedReader(new FileReader(args[1]));
			String strReceiver = "";
			Session session;
			
			//Each sender
			for(int k=0; k<senderNumber; k++) {
				//Assign a new session for a sender
				String uname = username[k];
				String pass = password[k];
				session = Session.getInstance(prop, new javax.mail.Authenticator() {
	                        protected PasswordAuthentication getPasswordAuthentication() {
	                            return new PasswordAuthentication(uname, pass);
	                        }
	            });
				
				//Each sender will send MAX_EMAIL_PER_USER emails
				for(int j=0; j<MAX_EMAIL_PER_USER; j++) {
					strReceiver = receiverReader.readLine();
					
					//Check if there still be a receiver to send
					if(strReceiver == null) {
						logWriter.println(dateFormat.format(date.getTime()) + " There is no more receiver email to send! Exit the app.");
						System.out.println("There is no more receiver email to send! Exit the app.");
						receiverReader.close();
						System.exit(0);
					}
										
					try {						
			            Message message = new MimeMessage(session);
			            message.setFrom(new InternetAddress(uname));
			            message.setRecipients(
			                    Message.RecipientType.TO,
			                    InternetAddress.parse(strReceiver)
			            );
			            message.setSubject(subject);		            
			            
			            message.setText(emailContent);
			            
			            //Send the email
			            Transport.send(message);
			            sentEmailNumber++;
			            logWriter.println(dateFormat.format(date) + " " + uname + " sends email to " + strReceiver);
			            System.out.println(sentEmailNumber + ". " +  uname + " sends email to " + strReceiver);

			        } catch (MessagingException e) {
			            e.printStackTrace();
			        }
				}
				
			}
			
			receiverReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        logWriter.println(dateFormat.format(date) + " Done");
        System.out.println("Done - Totally " + sentEmailNumber + " emails have been sent!");
    }
    
    /*public static void writeLog(String strLog) {    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	
    	try {
			PrintWriter logWriter = new PrintWriter("log.txt", "UTF-8");
			Date date = new Date();
			logWriter.println(dateFormat.format(date) + strLog);
			logWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }*/

}
