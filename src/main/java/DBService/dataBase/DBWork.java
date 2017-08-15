package DBService.dataBase;

import DBService.model.*;
import DBService.model.forOtherServices.User;

import java.util.Collection;

/**
 * Created by Nik on 20.06.2017.
 */
public interface DBWork {

    DataBase getDataBase();

    void cleanAll();
    void clean(String type);

    // для обновления списка фильмов
    void setFilms(Collection<Film> films);
    void setReserve(Collection<Reserve> reserves);
    void setShow(Collection<Show> shows);
    void setTickets(Collection<Ticket> tickets);

    // получение всех фильмов
    Collection<Film> getFilms();
    Collection<Room> getRooms();
    Collection<Film> getCinemas();
    Collection<Ticket> getTicketsById(Collection<Integer> ids);
    Collection<String> getCinemaNames();
    Collection<Film> getCinemasByFilm(String filmName);
    Collection<Reserve> getReserveByShowId(int showId);

    Boolean saveUser(User user);
    Boolean getUser(User user);

    Integer getShowId(String filmName, String cinemaName, int room_id);

    int getMaxIdRoom();
    Room getMaxPositionOfRoom(int room_id);

    Collection<Film> getFilmsFromShowsByCinemaName(String cinemaName);
    Collection<Show> getShowsForFilmCinema(String filmName, String cinemaName);
}
