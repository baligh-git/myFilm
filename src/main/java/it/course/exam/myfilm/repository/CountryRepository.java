package it.course.exam.myfilm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilm.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

}
