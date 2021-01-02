package it.course.exam.myfilm.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class FilmResponsePaged {
	
	private long pageNumber;
	private List<FilmResponse> pageResponse;
	private long totalePage;

}
