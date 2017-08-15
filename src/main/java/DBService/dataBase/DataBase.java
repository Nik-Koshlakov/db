package DBService.dataBase;

import DBService.model.*;
import DBService.model.forOtherServices.User;

import java.sql.Connection;
import java.util.Collection;

/**
 * Created by Nik on 20.06.2017.
 */
/*
    Shows - сеансы на фильм
    Films - фильмы и кинотеатры
    Reserve - купленные билеты на сеанс
    Tickets - илеты на сеансы
    Rooms - комнаты в кинотеатре
 */
public interface DataBase {
    Connection getConnection();
    void closeConnection();

    void clean(String truncate, String resetIncrement);

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
    Collection<Reserve> getReserveByShowId(int showId);
    Collection<String> getCinemaNames();
    Collection<Film> getCinemasByFilm(String filmName);
    Integer getShowId(String filmName, String cinemaName, int room_id);
    Collection<Show> getShowsForFilmCinema(String filmName, String cinemaName);

    Boolean saveUser(User user);
    Boolean getUser(User user);

    int getMaxIdRoom();
    Room getMaxPositionOfRoom(int room_id);

    Collection<Film> getFilmsFromShowsByCinemaName(String cinemaName);
}
