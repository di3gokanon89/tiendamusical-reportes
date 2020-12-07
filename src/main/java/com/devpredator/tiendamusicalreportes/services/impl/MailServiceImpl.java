/**
 * 
 */
package com.devpredator.tiendamusicalreportes.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.devpredator.tiendamusicalreportes.services.MailService;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * @author DevPredator
 * Clase que implementa los metodos para realizar la logica de negocio para enviar correos.
 */
@Service
public class MailServiceImpl implements MailService {
	
	@Value("${spring.mail.aws.smtp.host}")
	String host;

	@Value("${spring.mail.aws.smtp.user}")
	String user;
	
	@Value("${spring.mail.aws.smtp.password}")
	String password;
	
	@Value("${spring.mail.aws.smtp.port}")
	String port;
	
	@Value("${spring.mail.aws.smtp.sender}")
	String sender;
	
	@Value("${spring.mail.aws.smtp.starttls.enable}")
	String starttls;
	
	@Value("${spring.dropbox.directorio.reportes}")
	String pathReportesDropbox;
	
	@Override
	public Response enviarEmail(DbxClientV2 dbxClientV2, String destinatario, String cliente, String orderID) {

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", this.host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", this.starttls);
		properties.put("mail.smtp.port", this.port);

		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {	
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			message.setSubject("Compra Realizada Exitósamente - " + orderID);
			
			
			ByteArrayOutputStream archivoBytes = new ByteArrayOutputStream();

			DbxDownloader<FileMetadata> downloader = dbxClientV2.files().download(this.pathReportesDropbox + "/" + cliente + "/" + orderID + ".pdf");
			downloader.download(archivoBytes);

			BodyPart bodyPartText = new MimeBodyPart();
			bodyPartText.setText("Has realizado tu compra con éxito, adjunto a este correo podrás encontrar tu comprobante de pago para reclamar tus productos.");

			byte[] bytes = archivoBytes.toByteArray();
			InputStream inputStream = new ByteArrayInputStream(bytes);
	        ByteArrayDataSource ds = new ByteArrayDataSource(inputStream, "application/pdf"); 
			
			// 4) create new MimeBodyPart object and set DataHandler object to this object
			BodyPart bodyPartFile = new MimeBodyPart();
			
			// 5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPartText);
			bodyPartFile.setDataHandler(new DataHandler(ds));
			bodyPartFile.setFileName("COMPROBANTE-" + orderID + ".pdf");
			multipart.addBodyPart(bodyPartFile);

			message.setContent(multipart);

            AmazonSimpleEmailService ses = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
            
            PrintStream out = System.out;
            message.writeTo(out);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            
            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
            
            ses.sendRawEmail(rawEmailRequest);
            
			return Response.ok().build();
		} catch (MessagingException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		} catch (DownloadErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
		
	}

}
