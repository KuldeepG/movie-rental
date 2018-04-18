package com.thoughtworks.movierental.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
public class Rental {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name="DAYS_RENTED")
  private int daysRented;

  @Column(name="START_DATE")
  private Date startDate;

  @JoinColumn(name="MOVIE_ID")
  private Movie movie;

  @ManyToOne
  private Customer customer;

  protected Rental(){}

  public Rental(Movie movie, int daysRented) {
    this(movie, daysRented, Date.valueOf(LocalDate.now()));
  }

  Rental(Movie movie, int daysRented, Date startDate){
    this.movie = movie;
    this.daysRented = daysRented;
    this.startDate = startDate;
  }

  public int getDaysRented() {
    return daysRented;
  }

  public Movie getMovie() {
    return movie;
  }

  public Customer getCustomer() {
    return customer;
  }

  public LocalDate getStartDate() {
    return startDate.toLocalDate();
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

}