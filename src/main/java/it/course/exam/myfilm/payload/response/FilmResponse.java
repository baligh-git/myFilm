package it.course.exam.myfilm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
public class FilmResponse {
	
	private String film_id;
	private String title;
	private String description;
	private int release_year;
	private String language_name;
	private String country_name;
	
	

}
