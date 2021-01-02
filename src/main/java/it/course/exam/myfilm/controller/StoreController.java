package it.course.exam.myfilm.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilm.Service.FilmService;
import it.course.exam.myfilm.entity.Film;
import it.course.exam.myfilm.entity.Store;
import it.course.exam.myfilm.payload.request.StoreRequest;
import it.course.exam.myfilm.payload.response.ApiResponseCustom;
import it.course.exam.myfilm.repository.ActorRepository;
import it.course.exam.myfilm.repository.CountryRepository;
import it.course.exam.myfilm.repository.FilmRepository;
import it.course.exam.myfilm.repository.LanguageRepository;
import it.course.exam.myfilm.repository.StoreRepository;

@RestController
@Validated
public class StoreController {
	
	@Autowired
	FilmRepository filmRepository;
	@Autowired
	FilmService filmService;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired ActorRepository actorRepository;
	
	//******
	
	@PostMapping("/add-update-store")
	public ResponseEntity<ApiResponseCustom> addUpdateStore(@Valid @RequestBody StoreRequest storeRequest, 
			HttpServletRequest request) {
		
		Optional<Store> store=storeRepository.findById(storeRequest.getStoreId());
		
			
		String msg;
		if(store.isPresent()) {
			if(!store.get().getStoreName().equals(storeRequest.getStoreName())) {
				boolean exist=storeRepository.existsByStoreName(storeRequest.getStoreName());
				if(exist)
					return new ResponseEntity<ApiResponseCustom>(
							new ApiResponseCustom(
								Instant.now(), 403,	"FORBIDDEN", "this store name already exist", request.getRequestURI()
							), HttpStatus.FORBIDDEN);
			}
			
			
			msg="store : "+storeRequest.getStoreId()+" updated";
			store.get().setStoreName(storeRequest.getStoreName());
			storeRepository.save(store.get());
		}else {
			boolean exist=storeRepository.existsByStoreName(storeRequest.getStoreName());
			if(exist)
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 403,	"FORBIDDEN", "this store name already exist", request.getRequestURI()
						), HttpStatus.FORBIDDEN);
			msg="added new store "+storeRequest.getStoreId();
			Store s=new Store(storeRequest.getStoreId(),storeRequest.getStoreName());
			storeRepository.save(s);
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", msg, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	//*****
	
	@PostMapping("/add-film-to-store/{storeId}/{filmId}")
	public ResponseEntity<ApiResponseCustom> addFilmToStore(
			@PathVariable @Size(max=10,min=1) @NotNull  @NotEmpty String storeId,
			@PathVariable @Size(max=10,min=1) @NotNull  @NotEmpty String filmId, 
			HttpServletRequest request) {
		//@Size(max=10,min=1) @NotNull  @NotEmpty 
		Optional<Store> store=storeRepository.getStoreByStoreId(storeId);
		if(!store.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
				  new ApiResponseCustom(
					Instant.now(), 404,	"NOT FOUND", "Store not found", request.getRequestURI()
						), HttpStatus.NOT_FOUND);
		
		Optional<Film> film=filmRepository.findById(filmId);
		if(!film.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
				  new ApiResponseCustom(
					Instant.now(), 404,	"NOT FOUND", "film not found", request.getRequestURI()
						), HttpStatus.NOT_FOUND);
		
		Optional<Store> filmInStore=storeRepository.getStoreByFilmId(filmId);
		if(filmInStore.isPresent())
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 403,	"FORBIDDEN", "this film  already exist in another store "+filmInStore.get().getStoreId(), request.getRequestURI()
				), HttpStatus.FORBIDDEN);
		
		if(store.get().getFilms().contains(film.get()))
			return new ResponseEntity<ApiResponseCustom>(
				  new ApiResponseCustom(
					Instant.now(), 2000,	"NOT FOUND", "film "+filmId+" already exist in this store :"+storeId, request.getRequestURI()
						), HttpStatus.OK);
		
		store.get().getFilms().add(film.get());
		storeRepository.save(store.get());
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", "film "+filmId+" added to store :"+storeId, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	
}
