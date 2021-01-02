package it.course.exam.myfilm.Service;

import org.springframework.stereotype.Service;

import it.course.exam.myfilm.payload.response.FilmResponse;
import it.course.exam.myfilm.repository.FilmRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class FilmService {
	@Autowired
	FilmRepository filmRepository;
	
    public List<FilmResponse> findfilmsPagedTitleAsc(int pageNo,int pageSize){
		
		Pageable paging= PageRequest.of(pageNo, pageSize);
		Page<FilmResponse> pageResult=filmRepository.getAllFilmPagedByTitleAsc( paging);
		if(pageResult.hasContent()) {
			return pageResult.getContent();
		}else {
			return new ArrayList<FilmResponse>();
			
		}
	}	
    
    public boolean existsTitle(String title) {
    	return filmRepository.existsByTitle(title);
    }

}
