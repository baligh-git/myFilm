package it.course.exam.myfilm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class StoreRequest {
	
	@Size(max=10,min=1,message="film ID length must be between 1 a 10 chars")
	@NotBlank(message="Store Id must not be blank")
	@NotNull  @NotEmpty
    private String  storeId;

    @Size(max=50,min=1,message="Store Name length must be between 1 a 50 chars")
    @NotBlank(message="Store Name must not be blank")
    @NotEmpty
	private String storeName;
    
  
	
	

}
