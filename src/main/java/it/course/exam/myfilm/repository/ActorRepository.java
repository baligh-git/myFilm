package it.course.exam.myfilm.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilm.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String> {

	List<Actor> findAllByLastNameIn(Set<String> actors);

}
