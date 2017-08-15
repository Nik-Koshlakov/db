package DBService.dataBase;

import DBService.Constants;
import DBService.model.*;
import DBService.model.forOtherServices.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Nik on 20.06.2017.
 */
@Service("dbWork")
public class DBWorkImpl implements DBWork {

    @Autowired
    private DataBase dbImpl;

    @Override
    public DataBase getDataBase() {
        return dbImpl;
    }

    @Override
    public void cleanAll() {
        clean("films");
        clean("shows");
        clean("reserve");
        clean("tickets");
    }
    @Override
    public void clean(String type) {
        switch(type)
        {
            case "films" :
                dbImpl.clean(Constants.TRUNCATE_FILMS, Constants.RESET_AUTOINCREMENT_FILMS);
                break;
            case "shows" :
                dbImpl.clean(Constants.TRUNCATE_SHOWS, Constants.RESET_AUTOINCREMENT_SHOWS);
                break;
            case "reserve" :
                dbImpl.clean(Constants.TRUNCATE_RESERVE, Constants.RESET_AUTOINCREMENT_RESERVE);
                break;
            case "tickets" :
                dbImpl.clean(Constants.TRUNCATE_TICKETS, Constants.RESET_AUTOINCREMENT_TICKETS);
                break;
        }
    }

    @Override
    public void setFilms(Collection<Film> films) {
        dbImpl.setFilms(films);
    }
    @Override
    public void setReserve(Collection<Reserve> reserves) {
        dbImpl.setReserve(reserves);
    }
    @Override
    public void setShow(Collection<Show> shows) {
        dbImpl.setShow(shows);
    }
    @Override
    public void setTickets(Collection<Ticket> tickets) {
        dbImpl.setTickets(tickets);
    }

    @Override
    public Collection<Film> getFilms() {
        return dbImpl.getFilms();
    }
    @Override
    public Collection<Room> getRooms() {
        return dbImpl.getRooms();
    }
    @Override
    public Collection<Film> getCinemas() {
        return dbImpl.getCinemas();
    }
    @Override
    public Collection<String> getCinemaNames() {
        return dbImpl.getCinemaNames();
    }

    @Override
    public Collection<Film> getCinemasByFilm(String filmName) {
        return dbImpl.getCinemasByFilm(filmName);
    }
    @Override
    public Collection<Show> getShowsForFilmCinema(String filmName, String cinemaName) {
        return dbImpl.getShowsForFilmCinema(filmName, cinemaName);
    }

    @Override
    public Boolean saveUser(User user) {
        return dbImpl.saveUser(user);
    }
    @Override
    public Boolean getUser(User user) {
        return dbImpl.getUser(user);
    }

    @Override
    public Integer getShowId(String filmName, String cinemaName, int room_id) {
        return dbImpl.getShowId(filmName, cinemaName, room_id);
    }

    @Override
    public Collection<Reserve> getReserveByShowId(int showId) {
        return dbImpl.getReserveByShowId(showId);
    }

    @Override
    public int getMaxIdRoom() {
        return dbImpl.getMaxIdRoom();
    }

    @Override
    public Room getMaxPositionOfRoom(int room_id) {
        return dbImpl.getMaxPositionOfRoom(room_id);
    }

    @Override
    public Collection<Film> getFilmsFromShowsByCinemaName(String cinemaName) {
        return dbImpl.getFilmsFromShowsByCinemaName(cinemaName);
    }

    @Override
    public Collection<Ticket> getTicketsById(Collection<Integer> ids) {
        return dbImpl.getTicketsById(ids);
    }
}
