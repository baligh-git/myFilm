package it.course.exam.myfilm;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@SpringBootTest
@ContextConfiguration(classes = MyfilmApplication.class)
public class FilmtTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @BeforeEach
    public void setup(){
       this.mockMvc=MockMvcBuilders.webAppContextSetup(wac).build();

    }

    private String jsonFilm="{\n"
	+ "        \"filmId\": \"FILM099xx\",\r\n"
	+ "        \"title\": \"7anafi\",\r\n"
	+ "        \"description\": \"A Thoughtful Reflection of a Waitress And a Feminist who must Escape a Squirrel in A Manhattan Penthouse\",\r\n"
	+ "        \"releaseYear\": 1989,\r\n"
	+ "        \"language\": \"AE\",\r\n"
	+ "        \"country\": \"TN\"\r\n"
	+ "    }";



    //private String idFilm = "FILM099xx";
    @Test
    public void findFilmById()throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/add-update-film/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilm)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
				.andDo(print());
    }
    
}