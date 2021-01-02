package it.course.exam.myfilm.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.exam.myfilm.Service.FilmService;
import it.course.exam.myfilm.entity.Actor;
import it.course.exam.myfilm.entity.Country;
import it.course.exam.myfilm.entity.Film;
import it.course.exam.myfilm.entity.Language;
import it.course.exam.myfilm.payload.request.FilmRequest;
import it.course.exam.myfilm.payload.response.ApiResponseCustom;
import it.course.exam.myfilm.payload.response.FilmResponse;
import it.course.exam.myfilm.payload.response.FilmResponsePaged;
import it.course.exam.myfilm.payload.response.FilmsActorResponse;
import it.course.exam.myfilm.repository.ActorRepository;
import it.course.exam.myfilm.repository.CountryRepository;
import it.course.exam.myfilm.repository.FilmRepository;
import it.course.exam.myfilm.repository.LanguageRepository;
import it.course.exam.myfilm.repository.StoreRepository;



@RestController
@Validated
public class FilmController {
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
	
	
	//**********
	
	@PostMapping("/add-update-film")
	public ResponseEntity<ApiResponseCustom> addUpdateFilm(@Valid @RequestBody FilmRequest filmRequest, 
			HttpServletRequest request) {
		
		Optional<Language>  lang=languageRepository.findById(filmRequest.getLanguage());
		if(!lang.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Language not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Optional<Country>  country=countryRepository.findById(filmRequest.getCountry());
		if(!country.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Country not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Optional<Film> film=filmRepository.findById(filmRequest.getFilmId());
		
		String msg;
		if(film.isPresent()) {
			if(!filmRequest.getTitle().equals(film.get().getTitle())) {
				
				if(filmService.existsTitle(filmRequest.getTitle()))
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 403,	"FORBIDDEN", "Cannot be updated film : "+filmRequest.getFilmId()+" with title already exist", request.getRequestURI()
						), HttpStatus.FORBIDDEN);
				
			}
				
			
			msg="update film";
			film.get().setTitle(filmRequest.getTitle());
			film.get().setDescription(filmRequest.getDescription());
			film.get().setReleaseYear(filmRequest.getReleaseYear());
			film.get().setLanguage(lang.get());
			film.get().setCountry(country.get());
			filmRepository.save(film.get());
			
		}else {
			if(filmService.existsTitle(filmRequest.getTitle()))
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 403,	"FORBIDDEN","Cannot be add film : "+filmRequest.getFilmId()+" with title already exist", request.getRequestURI()
						), HttpStatus.FORBIDDEN);
			msg="new film added";
			Film f=new Film(
					filmRequest.getFilmId(),
					filmRequest.getTitle(),
					filmRequest.getDescription(),
					filmRequest.getReleaseYear(),
					lang.get(),
					country.get()
					);
			
			filmRepository.save(f);
		}
			
		
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", msg, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	//****
	
	@GetMapping("/get-film/{filmId}")
	public ResponseEntity<ApiResponseCustom> getFilmById(@PathVariable  @Size(max=10,min=1) @NotNull  String filmId, 
			HttpServletRequest request) {
		
		Optional<FilmResponse> film=filmRepository.getfilmById(filmId);
		if(!film.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", film.get(), request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@GetMapping("/get-films-paged-by-title-asc")
	public ResponseEntity<ApiResponseCustom> getFilmPagedByTitleAsc(
			@RequestParam(defaultValue = "0") int pagNo,
			@RequestParam(defaultValue = "2") int pagSize,
			HttpServletRequest request) {
		
		List<FilmResponse> film=filmService.findfilmsPagedTitleAsc(pagNo,pagSize);
		if(film.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		long totPost=filmRepository.count();
		FilmResponsePaged filmRes= new FilmResponsePaged(
				pagNo,
				film,
				((totPost/pagSize) +(totPost % pagSize > 0 ? 1 : 0))
				);
	
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", filmRes, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	//****
	
	@GetMapping("/find-film-in-store/{filmId}")
	public ResponseEntity<ApiResponseCustom> findFilmInStroe(@PathVariable @Size(max=10,min=1) @NotNull  @NotEmpty String filmId,HttpServletRequest request) {
		
		Optional<FilmResponse> film=filmRepository.getfilmById(filmId);
		if(!film.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		Optional<String> filmInStore=storeRepository.getStoreNameByFilmId(filmId);
		if(!filmInStore.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not present in any store ", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", "the film with id : "+filmId+" is in store :"+filmInStore.get(), request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	//*****
	
	@GetMapping("/find-films-by-actors")
	public ResponseEntity<ApiResponseCustom> findFilmsByActors(@RequestParam Set<String> actors,HttpServletRequest request) {
		
		List<Actor> listeActor=actorRepository.findAllByLastNameIn(actors);
		if(listeActor.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "actors not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		List<FilmResponse> films=filmRepository.getAllFilmByActors(listeActor,Long.valueOf(listeActor.size()));
		List<String> liste=listeActor.stream().map(Actor::toString).collect(Collectors.toList());
		if(films.isEmpty()) 
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "these actors "+liste+" are not featured in the same film", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		
		FilmsActorResponse far=new FilmsActorResponse(liste,films);
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", far, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@GetMapping("/find-films-by-country/{countryId}")
	public ResponseEntity<ApiResponseCustom> getFilmByCountry(@PathVariable @Size(max=2,min=1) @NotNull  @NotEmpty String countryId, 
			HttpServletRequest request) {
		
		 boolean exist=countryRepository.existsById(countryId);
		if(!exist)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Country  not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		List<FilmResponse> film=filmRepository.getFilmsByCountryId(countryId);
		if(film.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not product in this country : "+countryId, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", film, request.getRequestURI()
				), HttpStatus.OK);
		
		
	}
	
	@GetMapping("/find-films-by-language/{languageId}")
	public ResponseEntity<ApiResponseCustom> getFilmByLanguage(@PathVariable @Size(max=2,min=1) @NotNull  @NotEmpty String languageId, 
			HttpServletRequest request) {
		
		 boolean exist=languageRepository.existsById(languageId);
		if(!exist)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Language not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		List<FilmResponse> film=filmRepository.getFilmsBylanguageId(languageId);
		if(film.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"NOT FOUND", "Film not found for this language : "+languageId, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", film, request.getRequestURI()
				), HttpStatus.OK);
		
		
	}
	
	
	
	
}
	
	
	
