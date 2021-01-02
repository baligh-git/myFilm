package it.course.exam.myfilm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CUSTOMER")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Customer {
	
	
	@Id
	@Column(name="EMAIL",length=50)
	private String  email;
	
	@Column(name="FIRST_NAME",length=45)
	private String firstName;
	
	@Column(name="LAST_NAME",length=45)
	private String lastName;
	
	

}
