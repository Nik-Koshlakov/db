package DBService.web;

import DBService.dataBase.DBWork;
import DBService.model.*;
import DBService.model.forOtherServices.InformationAboutSession;
import DBService.model.forOtherServices.User;
import DBService.service.GenerateSecretKey;
import DBService.service.utils.ModifyStringSplits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Nik on 20.06.2017.
 */
@RestController
@RequestMapping("/")
public class DBController {

    @Autowired
    private DBWork dbWork;

    @RequestMapping(value = "/getFilms", method = RequestMethod.GET)
    public Collection<Film> getFilms() {
        return dbWork.getFilms();
    }

    @RequestMapping(value = "/getCinemas", method = RequestMethod.GET)
    public Collection<Film> getCinemas() {
        return dbWork.getCinemas();
    }

    @RequestMapping(value = "/getRooms", method = RequestMethod.GET)
    public Collection<Room> getRooms() {
        return dbWork.getRooms();
    }

    @RequestMapping(value = "/getShowsForFilmCinema/{filmName}/{cinemaName}", method = RequestMethod.GET)
    public Collection<Show> getShowsForFilmCinema(@PathVariable String filmName, @PathVariable String cinemaName) {
        return dbWork.getShowsForFilmCinema(
                ModifyStringSplits.modify(filmName),
                ModifyStringSplits.modify(cinemaName)
        );
    }

    @RequestMapping(value = "/getCinemasByFilm/{filmName}", method = RequestMethod.GET)
    public Collection<Film> getCinemasByFilm(@PathVariable String filmName) {
        return dbWork.getCinemasByFilm(ModifyStringSplits.modify(filmName));
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public boolean saveUser(@RequestBody User user) {
        return dbWork.saveUser(user);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public boolean getUser(@RequestBody User user) {
        return dbWork.getUser(user);
    }

    @RequestMapping(value = "/setReserve", method = RequestMethod.POST)
    public Collection<Reserve> setReserve(@RequestBody InformationAboutSession aboutSessions) {
        List<Ticket> ticketList = new ArrayList<>();
        List<Integer> row = aboutSessions.getTicket_row();
        List<Integer> place = aboutSessions.getTicket_place();
        Collection<Reserve> reserveCollection = new ArrayList<>();

        int roomId = aboutSessions.getRoom_id();
        int showId = aboutSessions.getShow_id();
        String film = aboutSessions.getFilm_name();
        String cinema = aboutSessions.getFilm_nameCinema();
        String visitorName = aboutSessions.getVisitorName();

        for (int i = 0; i < row.size(); i++)
            ticketList.add(new Ticket(null, roomId, row.get(i), place.get(i)));
        dbWork.setTickets(ticketList);

        for (Ticket ticket : ticketList) {
            String code = GenerateSecretKey.generate(visitorName, film, cinema);
            reserveCollection.add(new Reserve(null, showId, ticket.getTicket_id(), visitorName, code));
        }
        dbWork.setReserve(reserveCollection);

        return reserveCollection;
    }

    @RequestMapping(value = "/getBougthTickets/{filmName}/{cinemaName}/{room_id}", method = RequestMethod.GET)
    public Collection<Ticket> getBougthTickets(@PathVariable String filmName,
                                              @PathVariable String cinemaName, @PathVariable int room_id) {
        int showId = dbWork.getShowId(ModifyStringSplits.modify(filmName),
                ModifyStringSplits.modify(cinemaName), room_id);

        Collection<Reserve> reserveCollection = dbWork.getReserveByShowId(showId);

        return dbWork.getTicketsById(getTicketIds(reserveCollection));
    }

    @RequestMapping(value = "/getBougthTickets/{show_id}", method = RequestMethod.GET)
    public Collection<Ticket> getBougthTickets(@PathVariable int show_id) {
        Collection<Reserve> reserveCollection = dbWork.getReserveByShowId(show_id);

        return dbWork.getTicketsById(getTicketIds(reserveCollection));
    }

    private List<Integer> getTicketIds(Collection<Reserve> list) {
        List<Integer> result = new ArrayList<>();
        for (Reserve reserve : list)
            result.add(reserve.getTicket_id());
        return result;
    }

    @RequestMapping(value = "/getMaxPositionRoom={room_id}", method = RequestMethod.GET)
    public Room getMaxPositionOfRoom(@PathVariable int room_id) {
        return dbWork.getMaxPositionOfRoom(room_id);
    }
}
