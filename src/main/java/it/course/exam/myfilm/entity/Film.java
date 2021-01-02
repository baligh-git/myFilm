package it.course.exam.myfilm.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="FILM")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Film {
	
	
	@Id
	@Column(name="FILM_ID",length=10)
	private String  filmId;
	
	@Column(name="TITLE",nullable=false,length=128)
	private String title;
	
	@Column(name="DESCRIPTION",columnDefinition = "TEXT NOT NULL")
	private String description;
	
	@Column(name="RELEASE_YEAR",nullable=false)
	private int releaseYear;
	
	@ManyToOne
	@JoinColumn(name = "LANGUAGE_ID",nullable=false)
	private Language language;
	
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID",nullable=false)
	private Country country;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="FILM_ACTOR",
	joinColumns = {@JoinColumn(name="FILM_ID")},
	inverseJoinColumns = {@JoinColumn(name="ACTOR_ID")}
	)
	private Set<Actor> actors;

	public Film(String filmId, String title, String description, int releaseYear, Language language, Country country) {
		super();
		this.filmId = filmId;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.language = language;
		this.country = country;
	}

	public Film(String filmId) {
		super();
		this.filmId = filmId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filmId == null) ? 0 : filmId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (filmId == null) {
			if (other.filmId != null)
				return false;
		} else if (!filmId.equals(other.filmId))
			return false;
		return true;
	}

	

	

	
	
	
	
	

}
