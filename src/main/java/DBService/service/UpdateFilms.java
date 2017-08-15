package DBService.service;

import DBService.dataBase.DBWork;
import DBService.model.forOtherServices.DataFromServiceKudaGo;
import DBService.model.Film;
import DBService.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Nik on 20.06.2017.
 */
@Component
public class UpdateFilms {

    @Autowired
    private DBWork dbWork;

    // each 30 sec ("0/30 * * * * *"), each hour ("0 0 * * * *")
    //@Scheduled(cron = "0 0 0 * * *")
    public void update() {

        dbWork.cleanAll();

        List<DataFromServiceKudaGo> list = requestDataFromService();

        List<Film> films = new ArrayList<>();
        for (DataFromServiceKudaGo data : list) {
            Film film = new Film(list.indexOf(data), data.getNameMovie(), data.getPosterMoviePath(),
                    data.getRunning_time(), data.getPrice(), data.getImax(),
                    data.getCountryFilm(), data.getYearFilm(), data.getTrailerFilm(),
                    data.getAgeFilm(), data.getDirectorFilm(), data.getNameCinema(),
                    data.getAddressCinema(), data.getPhone(), data.getStationAboutCinema(),
                    data.getPosterCinemaPath(), data.getBeginning_time(), data.getDescription());
            films.add(film);
        }

        dbWork.setFilms(films);

        fillShows();
    }

    private List<DataFromServiceKudaGo> requestDataFromService() {
        int tryCount = 0;
        List<DataFromServiceKudaGo> list = new ArrayList<>();

        while (list.isEmpty()) {
            try {
                tryCount++;

                SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
                s.setReadTimeout(6000);
                s.setConnectTimeout(1500);

                RestTemplate restTemplate = new RestTemplate(s);

                String address = "http://localhost:8083/getData";
                ResponseEntity<List<DataFromServiceKudaGo>> serviceKudaGo = restTemplate.exchange(
                        address,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<DataFromServiceKudaGo>>() {
                        }
                );

                if (serviceKudaGo.getStatusCode() != HttpStatus.OK)
                    System.out.println("Server answered wrong or maybe he broke");

                list = serviceKudaGo.getBody();
            } catch (ResourceAccessException exception) {
                System.out.println("Server is`t available");
            }
        }

        System.out.println("Number of requests to serviceKudaGo: " + tryCount);

        return list;
    }

    private void fillShows() {
        int maxIdRoom = dbWork.getMaxIdRoom();
        List<Show> shows = new ArrayList<>();
        Collection<String> cinemaNames = dbWork.getCinemaNames();

        for (String cinemaName : cinemaNames) {
            int countRoom = 1;
            Collection<Film> filmsForShow = dbWork.getFilmsFromShowsByCinemaName(cinemaName);

            for (Film filmForShow : filmsForShow) {
                Show show = new Show();
                show.setFilm_nameCinema(filmForShow.getFilm_nameCinema());
                show.setRoom_id(countRoom);
                show.setShow_time(filmForShow.getFilm_runningTime());
                show.setFilm_name(filmForShow.getFilm_name());
                countRoom++;
                if (countRoom > maxIdRoom)
                    countRoom = 1;

                shows.add(show);
            }
        }

        dbWork.setShow(shows);
    }
}
