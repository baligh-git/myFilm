package it.course.exam.myfilm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
public class FilmRentResponseNr {
	
	private String film_id;
	private String title;
	private long number_of_rents;
	
	

}
