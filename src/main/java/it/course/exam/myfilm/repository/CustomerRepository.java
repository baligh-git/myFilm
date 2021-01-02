package it.course.exam.myfilm.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilm.entity.Customer;
import it.course.exam.myfilm.payload.response.CustomerResponse;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	
	@Query(value="SELECT DISTINCT new it.course.exam.myfilm.payload.response.CustomerResponse("
			+ "c.email, "
			+ "c.firstName, "
			+ "c.lastName "
			+ ") "
			+ "FROM Customer c "
			+ "JOIN Rental r on r.rentalId.store.storeId=:storeId "
			+ "AND r.rentalId.customer=c ")
	Set<CustomerResponse> getCustomersByStore(@Param("storeId") String storeId);
	
	@Query(value="SELECT DISTINCT new it.course.exam.myfilm.payload.response.CustomerResponse("
			+ "c.email, "
			+ "c.firstName, "
			+ "c.lastName "
			+ ") "
			+ "FROM Customer c "
			+ "JOIN Rental r on r.rentalId.customer=c "
			+ "JOIN Film f on r.rentalId.film=f  "
			+ "AND f.language.languageId=:languageId ")
	Set<CustomerResponse> getCustomersByRentFilmByLanguage(@Param("languageId") String languageId);

}
