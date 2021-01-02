package it.course.exam.myfilm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @AllArgsConstructor @NoArgsConstructor
public class RentalId implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Column(name="RENTAL_DATE",columnDefinition ="Date")
	private Date rentalDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_EMAIL", nullable=false)
	private Customer customer;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FILM_ID", nullable=false)
	private Film film;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STORE_ID", nullable=false)
	private Store store;

}
