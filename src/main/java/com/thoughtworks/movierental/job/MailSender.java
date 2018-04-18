package com.thoughtworks.movierental.job;

import com.sun.mail.smtp.SMTPTransport;
import com.thoughtworks.movierental.model.Rental;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class MailSender {
    public void process() throws SQLException, IOException, InterruptedException, MessagingException {
        //Load configuration
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("movie_rental.properties");
        Properties properties = new Properties();
        properties.load(stream);
        stream.close();

        //Database entity manager
        String persistenceUnitName = properties.getProperty("persistence-unit.name");
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager em = emFactory.createEntityManager();

        //noinspection JpaQlInspection
        Query query = em.createQuery("SELECT r FROM Rental r");
        List<Rental> rentals = query.getResultList();

        for (Rental rental : rentals) {
            LocalDate dueDate = rental.getStartDate().plusDays(rental.getDaysRented());

            //Check if movie return is due in days according to configuration
            if (LocalDate.now().plusDays(parseInt(properties.getProperty("reminder.due.days"))).isEqual(dueDate)) {
                //send email
                Session session = Session.getInstance(properties);
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("no-reply@movierental.com"));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(rental.getCustomer().getEmail(), false));
                msg.setSubject("Reminder for movie - " + rental.getMovie().getTitle());
                msg.setText("Movie " + rental.getMovie().getTitle() + " return is due on " + dueDate.toString());
                msg.setSentDate(new Date());
                SMTPTransport t =
                        (SMTPTransport) session.getTransport("smtp");
                t.connect();
                t.sendMessage(msg, msg.getAllRecipients());
                t.close();
            }
        }

        em.close();
    }
}
