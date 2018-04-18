package com.thoughtworks.movierental.job;

import com.thoughtworks.movierental.model.Customer;
import com.thoughtworks.movierental.model.Movie;
import com.thoughtworks.movierental.model.Rental;

import javax.persistence.*;

public class DataSetup {

    public static void tearDown() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("movie_rental");
        EntityManager em
            = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery("DELETE from Rental r").executeUpdate();
        em.createQuery("DELETE from Movie m").executeUpdate();
        em.createQuery("DELETE from Customer c").executeUpdate();
        transaction.commit();
        em.close();

    }

    public static void setup() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("movie_rental");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Movie movie1 = new Movie("Rang de", Movie.REGULAR);
        Movie movie2 = new Movie("Jagga Jasoos", Movie.NEW_RELEASE);
        Movie movie3 = new Movie("Despicable me", Movie.CHILDRENS);
        Movie movie4 = new Movie("Gunda", Movie.REGULAR);

        em.merge(movie1);
        em.merge(movie2);
        em.merge(movie3);
        em.merge(movie4);

        Customer customer1 = new Customer("Amitabh Bacchan", "amitabh@bacchan.com");
        Customer customer2 = new Customer("Dharmendra Deol", "dharmendra@deol.com");
        Customer customer3 = new Customer("Jitendra", "jitendra@gmail.com");

        Rental rental1 = new Rental(movie1, 2);
        Rental rental2 = new Rental(movie2, 2);
        Rental rental3 = new Rental(movie3, 2);

        customer1.addRental(rental1);
        customer1.addRental(rental2);

        customer2.addRental(rental3);

        em.merge(customer1);
        em.merge(customer2);
        em.merge(customer3);

        transaction.commit();

        Query query = em.createQuery("SELECT count(c) from Customer c");
        Object result = query.getSingleResult();
        System.out.printf("======================================");
        System.out.println(result);

        em.close();
    }
}
