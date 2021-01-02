package it.course.exam.myfilm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ACTOR")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Actor {
	
	
	@Id
	@Column(name="ACTOR_ID",length=10)
	private String  actorId;
	
	@Column(name="FIRST_NAME",nullable=false,length=45)
	private String firstName;
	
	@Column(name="LAST_NAME",nullable=false,length=45)
	private String lastName;
	
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID",nullable=false)
	private Country country;

	@Override
	public String toString() {
		return "[" + firstName + "," + lastName + "]";
	}
	
	
	
	

}
