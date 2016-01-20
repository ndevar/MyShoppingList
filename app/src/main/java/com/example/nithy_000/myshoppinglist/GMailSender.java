package com.example.nithy_000.myshoppinglist;

import android.util.Log;
import android.widget.Toast;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.BodyPart;

import javax.mail.Message;

import javax.mail.MessagingException;
import javax.mail.Multipart;

import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeBodyPart;

import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

import java.io.ByteArrayInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.security.Security;

import java.util.Properties;


public class GMailSender extends javax.mail.Authenticator {

    private String mailhost = "smtp.gmail.com";

    private String user;

    private String password;

    private Session session;



    private Multipart _multipart = new MimeMultipart();

    static {

        Security.addProvider(new JSSEProvider());

    }



    public GMailSender(String user, String password) {

        this.user = user;

        this.password = password;



        Properties properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);


        session = Session.getDefaultInstance(properties, this);

    }



    protected PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(user, password);

    }



    public synchronized void sendMail(String subject, String body,

                                      String sender, String recipients) throws Exception {

        try {
            Log.d("test","Step1");
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipients));

            message.setSubject(subject);
            message.setContent(body,"text/html" );

            Transport.send(message);

            }
        catch (Exception e) {
        }

    }


    public void addAttachment(String filename) throws Exception {

        BodyPart messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(filename);

        messageBodyPart.setDataHandler(new DataHandler(source));

        messageBodyPart.setFileName("download image");



        _multipart.addBodyPart(messageBodyPart);

    }



    public class ByteArrayDataSource implements DataSource {

        private byte[] data;

        private String type;





        public ByteArrayDataSource(byte[] data, String type) {

            super();

            this.data = data;

            this.type = type;

        }



        public ByteArrayDataSource(byte[] data) {

            super();

            this.data = data;

        }





        public void setType(String type) {

            this.type = type;

        }



        public String getContentType() {

            if (type == null)

                return "application/octet-stream";

            else

                return type;

        }



        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(data);

        }



        public String getName() {

            return "ByteArrayDataSource";

        }



        public OutputStream getOutputStream() throws IOException {

            throw new IOException("Not Supported");

        }

    }

}
