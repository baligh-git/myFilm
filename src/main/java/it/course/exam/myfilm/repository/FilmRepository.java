package it.course.exam.myfilm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilm.entity.Actor;
import it.course.exam.myfilm.entity.Film;
import it.course.exam.myfilm.payload.response.FilmResponse;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {
	
		boolean existsByTitle(String title);
	
	   @Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmResponse("
			+ "f.filmId, "
			+ "f.title, "
			+ "f.description, "
			+ "f.releaseYear, "
			+ "f.language.languageName,"
			+ "f.country.countryName"		
			+ ") "
			+ "FROM Film f "
			+ "WHERE f.filmId = :filmId ")
		Optional<FilmResponse> getfilmById(@Param("filmId") String filmId);

	
	
	   @Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmResponse("
			+ "f.filmId, "
			+ "f.title, "
			+ "f.description, "
			+ "f.releaseYear, "
			+ "f.language.languageName,"
			+ "f.country.countryName"		
			+ ") "
			+ "FROM Film f "
			+ "ORDER BY f.title ASC ")
		Page<FilmResponse> getAllFilmPagedByTitleAsc(Pageable pageable);
	   
	   
	    @Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmResponse("
				+ "f.filmId, "
				+ "f.title, "
				+ "f.description, "
				+ "f.releaseYear, "
				+ "f.language.languageName,"
				+ "f.country.countryName"		
				+ ") "
				+ "FROM Film f "
				+ "JOIN f.actors a "
				+ "WHERE a IN :actors " 
				+ "GROUP BY f.filmId "
				+ "HAVING COUNT(f.filmId) =:size ")
		List<FilmResponse> getAllFilmByActors(@Param("actors") List<Actor> actors ,@Param("size") Long size);
	   
   

	    @Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmResponse("
				+ "f.filmId, "
				+ "f.title, "
				+ "f.description, "
				+ "f.releaseYear, "
				+ "f.language.languageName,"
				+ "f.country.countryName"		
				+ ") "
				+ "FROM Film f "
				+ "WHERE f.country.countryId = :countryId ")
		List<FilmResponse> getFilmsByCountryId(@Param("countryId") String countryId);
	    
	    @Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmResponse("
				+ "f.filmId, "
				+ "f.title, "
				+ "f.description, "
				+ "f.releaseYear, "
				+ "f.language.languageName,"
				+ "f.country.countryName"		
				+ ") "
				+ "FROM Film f "
				+ "WHERE f.language.languageId = :languageId ")
		List<FilmResponse> getFilmsBylanguageId(@Param("languageId") String languageId);


	
	
	

}
