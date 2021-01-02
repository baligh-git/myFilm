package it.course.exam.myfilm.controller;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilm.entity.Customer;
import it.course.exam.myfilm.payload.request.CustomerRequest;
import it.course.exam.myfilm.payload.response.ApiResponseCustom;
import it.course.exam.myfilm.payload.response.CustomerResponse;
import it.course.exam.myfilm.repository.CustomerRepository;
import it.course.exam.myfilm.repository.StoreRepository;

@RestController
@Validated
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	StoreRepository storeRepository;
	
	
	//*****
	
	@PostMapping("/add-update-customer")
	public ResponseEntity<ApiResponseCustom> addUpdateCustomer(@Valid @RequestBody CustomerRequest customerRequest, 
			HttpServletRequest request) {
		
		Optional<Customer> customer=customerRepository.findById(customerRequest.getEmail());
		
		
		String msg;
		if(customer.isPresent()) {
			msg="customer : "+customerRequest.getEmail()+" updated";
			customer.get().setFirstName(customerRequest.getFirstName());
			customer.get().setLastName(customerRequest.getLastName());
			customerRepository.save(customer.get());
		}else {
			msg="new customer "+customerRequest.getEmail()+" added";
			Customer c=new Customer(customerRequest.getEmail(),customerRequest.getFirstName(),customerRequest.getLastName());
			customerRepository.save(c);
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", msg, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	// ****
	
	@GetMapping("/get-all-customers-by-store/{storeId}")
	public ResponseEntity<ApiResponseCustom> getAllCutomersByStore(@PathVariable @Size(max=10,min=1) @NotNull  @NotEmpty String storeId,HttpServletRequest request) {
		
		boolean exist=storeRepository.existsById(storeId);
		if(!exist)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Store not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Set<CustomerResponse> cr=customerRepository.getCustomersByStore(storeId);
		if(cr.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "no customers found in the store", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", cr, request.getRequestURI()
				), HttpStatus.OK);
		
	}

}
