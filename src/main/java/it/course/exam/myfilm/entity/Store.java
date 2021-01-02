package it.course.exam.myfilm.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="STORE")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Store {
	
	
	@Id
	@Column(name="STORE_ID",length=10)
	private String  storeId;
	
	@Column(name="STORE_NAME",unique = true,length=50)
	private String storeName;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="INVENTORY",
	joinColumns = {@JoinColumn(name="STORE_ID")},
	inverseJoinColumns = {@JoinColumn(name="FILM_ID")}
	)
	private Set<Film> films;

	public Store(String storeId, String storeName) {
		super();
		this.storeId = storeId;
		this.storeName = storeName;
	}
	
	

}
