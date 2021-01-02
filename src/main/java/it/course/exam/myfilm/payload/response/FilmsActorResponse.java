package it.course.exam.myfilm.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class FilmsActorResponse {
	
	private List<String> actors;
	
	private List<FilmResponse> filmResponse;
	

}
