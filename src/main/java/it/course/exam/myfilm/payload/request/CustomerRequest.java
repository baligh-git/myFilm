package it.course.exam.myfilm.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class CustomerRequest {
	
	@Size(max=50,message="email ID length max 50 chars")
	@NotBlank(message="email Id must not be blank")
	@NotNull  @NotEmpty
	@Email(message = "Email should be valid")
    private String  email;

    @Size(max=50,message="first Name length max 45 chars")
	private String firstName;
    
    @Size(max=50,message="last Name length max 45 chars")
	private String lastName;
    
   
    
  
	
	

}
