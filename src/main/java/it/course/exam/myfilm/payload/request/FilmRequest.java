package it.course.exam.myfilm.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class FilmRequest {
	
	@Size(max=10,min=1,message="film ID length must be between 1 a 10 chars")
	@NotBlank(message="film Id must not be blank")
	@NotNull  @NotEmpty
    private String  filmId;

    @Size(max=128,min=1,message="Title length must be between 1 a 128 chars")
    @NotBlank(message="Title must not be blank")
    @NotEmpty
	private String title;
    
    @NotBlank(message="Title must not be blank")
    @NotNull  @NotEmpty
	private String description;
    
    @NotNull  
    @Min(value = 1800, message = "release year should not be less than 1800")
    @Max(value = 2100, message = "release year should not be greater than 2100")
	private int releaseYear;
	
    @NotNull  @NotEmpty
    @Size(max=2,min=1,message="language length must be between 1 a 2 chars")
	private String language;
	
    @NotNull  @NotEmpty
    @Size(max=2,min=1,message="country length must be between 1 a 2 chars")
	private String country;
	

}
