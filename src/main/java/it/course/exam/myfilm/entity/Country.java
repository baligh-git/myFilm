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
@Table(name="COUNTRY")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Country {
	
	
	@Id
	@Column(name="COUNTRY_ID",length=2)
	private String  countryId;
	
	@Column(name="COUNTRY_NAME",unique = true,nullable=false,length=40)
	private String countryName;
	
	

}
