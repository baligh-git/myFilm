package it.course.exam.myfilm.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilm.entity.Customer;
import it.course.exam.myfilm.entity.Film;
import it.course.exam.myfilm.entity.Rental;
import it.course.exam.myfilm.entity.RentalId;
import it.course.exam.myfilm.entity.Store;
import it.course.exam.myfilm.payload.request.RentalRequest;
import it.course.exam.myfilm.payload.response.ApiResponseCustom;
import it.course.exam.myfilm.payload.response.CustomerResponse;
import it.course.exam.myfilm.payload.response.FilmRentResponse;
import it.course.exam.myfilm.payload.response.FilmRentResponseNr;
import it.course.exam.myfilm.repository.CustomerRepository;
import it.course.exam.myfilm.repository.FilmRepository;
import it.course.exam.myfilm.repository.LanguageRepository;
import it.course.exam.myfilm.repository.RentalRepository;
import it.course.exam.myfilm.repository.StoreRepository;

@RestController
@Validated
public class RentalController {
	
	@Autowired
	FilmRepository filmRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired 
	CustomerRepository customerRepository;
	@Autowired
	RentalRepository rentalRepository;
	
	//*****
	
	@PostMapping("/add-update-rental")
	public ResponseEntity<ApiResponseCustom> addUpdateRental(@Valid @RequestBody RentalRequest rentalRequest, 
			HttpServletRequest request) {
		
		Optional<Customer> customer=customerRepository.findById(rentalRequest.getEmail());
		if(!customer.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Customer not found ", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Optional<Store> store=storeRepository.getStoreByStoreId(rentalRequest.getStoreId());
		if(!store.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "store not found ", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Optional<Film> film=filmRepository.findById(rentalRequest.getFilmId());
		if(!film.isPresent())
		 return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 404,	"NOT FOUND", "film not found ", request.getRequestURI()
				), HttpStatus.NOT_FOUND);
		
		if(!store.get().getFilms().contains(film.get()) )
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "film not found in the store", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		
		Optional<Rental> rental=rentalRepository.getRentalFilmForUpdate(store.get(),film.get(),customer.get());
		/*rental present -> update*/
		if(rental.isPresent()) {
			rental.get().setRentalReturn(new Date());
			rentalRepository.save(rental.get());
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "rental return updated", request.getRequestURI()
					), HttpStatus.OK);
		}
		/*rentalnot present -> add*/
		
		RentalId rentalId=new RentalId(new Date(),
				 customer.get(),
				 new Film(rentalRequest.getFilmId()),
				 store.get()
				 );
		rentalRepository.save(new Rental(rentalId,new Date(253402214400000L)));
			
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", "new rental added", request.getRequestURI()
				), HttpStatus.OK);
	
	}
	
	// *****
	
	@GetMapping("/get-rentals-number-in-date-range")
	public ResponseEntity<ApiResponseCustom> getRentalsNumberInDateRange(
			@RequestParam @Valid @NotNull @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate,
			@RequestParam @Valid @NotNull @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate endDate
			,HttpServletRequest request) {
		
		if(endDate.isBefore(startDate))
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 403,	"FORBIDDEN", "not valid start date end date", request.getRequestURI()
					), HttpStatus.FORBIDDEN);
		
		int count=rentalRepository.countRentalInDateRange(startDate,endDate);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", "number of rental from "+startDate+" to "+endDate+" is "+count, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	//*******
	
	@GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
	public ResponseEntity<ApiResponseCustom> findFilmsRentByCustomer(@PathVariable @Email @NotNull  @NotEmpty String customerId ,HttpServletRequest request) {
		
		boolean exist=customerRepository.existsById(customerId);
		if(!exist)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Customer not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Set<FilmRentResponse> frr=rentalRepository.getRentFilmsByCustomerId(customerId);
		if(frr.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "no rent present for this customer "+customerId, request.getRequestURI()
					), HttpStatus.NOT_FOUND);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",frr, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	//*******
	
	@GetMapping("/find-film-with-max-number-of-rent")
	public ResponseEntity<ApiResponseCustom> findFilmsWithMaxRent(HttpServletRequest request) {
		
		
		List<FilmRentResponseNr> frr=rentalRepository.getFilmWithMaxRent();
				               
		if(frr.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "no rent present  ", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		FilmRentResponseNr filmWithMaxRent=frr.get(0);
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",filmWithMaxRent, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	//******
	
	@GetMapping("/get-customers-who-rent-films-by-language-film/{languageId}")
	public ResponseEntity<ApiResponseCustom> findCustomersRentFilmByLanguage(@PathVariable  @Size(max=2,min=1) @NotNull  @NotEmpty String languageId 
			,HttpServletRequest request) {
		
		boolean exist=languageRepository.existsById(languageId);
		if(!exist)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "language not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		Set<CustomerResponse> cr=customerRepository.getCustomersByRentFilmByLanguage(languageId);
		if(cr.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "no rent present for this language "+languageId, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",cr, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
		
			

}
