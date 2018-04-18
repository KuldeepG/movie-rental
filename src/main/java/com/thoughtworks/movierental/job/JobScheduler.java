package com.thoughtworks.movierental.job;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public class JobScheduler {
    public static void main(String[] args) throws InterruptedException, SQLException, IOException, MessagingException {
        new MailSender().process();
    }
}
