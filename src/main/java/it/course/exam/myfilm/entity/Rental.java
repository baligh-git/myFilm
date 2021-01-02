package it.course.exam.myfilm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="RENTAL")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Rental {
	
	
	@EmbeddedId
	private RentalId  rentalId;
	
	@Column(name="RENTAL_RETURN",nullable = false,columnDefinition ="Date")
	private Date rentalReturn;

	public Rental(RentalId rentalId) {
		super();
		this.rentalId = rentalId;
	}
	
	
	
	
	

}
