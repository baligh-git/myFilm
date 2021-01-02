package it.course.exam.myfilm.payload.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class RentalRequest {
	
	@Size(max=10,min=1,message="film ID length must be between 1 a 10 chars")
	@NotBlank(message="film Id must not be blank")
	@NotNull  @NotEmpty
    private String  filmId;

    @Size(max=128,min=1,message="Title length must be between 1 a 128 chars")
    @NotBlank(message="Title must not be blank")
    @NotEmpty
	private String storeId;
    
    @NotBlank(message="Title must not be blank")
    @NotNull  @NotEmpty
    @Email
	private String email;
    
    
	
    
	

}
