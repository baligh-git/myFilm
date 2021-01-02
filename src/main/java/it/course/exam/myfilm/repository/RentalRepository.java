package it.course.exam.myfilm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilm.entity.Customer;
import it.course.exam.myfilm.entity.Film;
import it.course.exam.myfilm.entity.Rental;
import it.course.exam.myfilm.entity.RentalId;
import it.course.exam.myfilm.entity.Store;
import it.course.exam.myfilm.payload.response.FilmRentResponse;
import it.course.exam.myfilm.payload.response.FilmRentResponseNr;

@Repository
public interface RentalRepository extends JpaRepository<Rental, RentalId> {


	
	@Query(value = "Select count(*) from rental where rental_date between :startDate and :endDate ",nativeQuery = true)
	int countRentalInDateRange(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query(value="SELECT DISTINCT new it.course.exam.myfilm.payload.response.FilmRentResponse("
			+ "f.filmId, "
			+ "f.title "
			+ ") "
			+ "FROM Rental r "
			+ "JOIN Film f on f=r.rentalId.film "
			+ "WHERE r.rentalId.customer.email = :email ")
	Set<FilmRentResponse> getRentFilmsByCustomerId(@Param("email") String email);
	
	@Query(value="SELECT new it.course.exam.myfilm.payload.response.FilmRentResponseNr("
			+ "f.filmId, "
			+ "f.title, "
			+ "COUNT(r.rentalId.film) as counter ) "
			+ "FROM Rental r "
			+ "JOIN Film f on f=r.rentalId.film "
			+ "GROUP BY r.rentalId.film "
			+ "ORDER BY counter DESC")
	List<FilmRentResponseNr> getFilmWithMaxRent(); /*----> list.get(0)*/
	
	
	
	@Query(value="SELECT r from Rental r "
			+ "WHERE r.rentalId.store=:store "
			+ "AND r.rentalId.film=:film "
			+ "AND r.rentalId.customer=:customer "
			+ "AND r.rentalReturn >= CURRENT_DATE "
			
			      )
	Optional<Rental> getRentalFilmForUpdate(@Param("store") Store store,@Param("film") Film film,@Param("customer") Customer customer);
	

}
