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
@Table(name="LANGUAGE")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Language {
	
	
	@Id
	@Column(name="LANGUAGE_ID",length=2)
	private String  languageId;
	
	@Column(name="LANGUAGE_NAME",unique = true,nullable=false,length=40)
	private String languageName;

}
