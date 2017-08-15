package DBService.dataBase;

import DBService.Constants;
import DBService.model.*;
import DBService.model.forOtherServices.User;
import com.mysql.fabric.jdbc.FabricMySQLDriver;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Created by Nik on 20.06.2017.
 */
@Service("dbImpl")
public class DataBaseImpl implements DataBase {
    private Connection connection;
    private static final Logger LOG = Logger.getLogger(DataBaseImpl.class);

    public DataBaseImpl() {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            if (LOG.isInfoEnabled())
                LOG.info("Driver register");

            getConnection();
            closeConnection();
        } catch (SQLException e) {
            LOG.error("Could`t connect to database", e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(Constants.DB_URL,
                    Constants.DB_USER, Constants.DB_PASSWORD);
        } catch (SQLException e) {
            LOG.error("Couldn`t connect to db", e);
        } finally {
            return connection;
        }
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
            if (LOG.isInfoEnabled())
                LOG.info("Connection close");
        } catch (SQLException e) {
            LOG.error("Couldn`t clse connection", e);
        }
    }

    @Override
    public void setFilms(Collection<Film> films) {
        try {
            getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.INSERT_FILM);

            int max_len = -1;
            for (Film film : films) {
                preparedStatement.setString(1, film.getFilm_name());
                preparedStatement.setString(2, film.getFilm_image());
                preparedStatement.setInt(3, film.getFilm_length());
                preparedStatement.setDouble(4, film.getFilm_price());
                preparedStatement.setBoolean(5, film.getFilm_imax());
                preparedStatement.setString(6, film.getFilm_cuntry());
                preparedStatement.setString(7, film.getFilm_year());
                preparedStatement.setString(8, film.getFilm_trailer());
                preparedStatement.setString(9, film.getFilm_age());
                preparedStatement.setString(10, film.getFilm_derictor());
                preparedStatement.setString(11, film.getFilm_nameCinema());
                preparedStatement.setString(12, film.getFilm_addressCinema());
                preparedStatement.setString(13, film.getFilm_phone());
                preparedStatement.setString(14, film.getFilm_subway());
                preparedStatement.setString(15, film.getFilm_imageCinema());

                Object param = new java.sql.Timestamp(film.getFilm_runningTime().getTime());
                preparedStatement.setObject(16, param);
                preparedStatement.setString(17, film.getFilm_description());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            if (LOG.isInfoEnabled())
                LOG.info("Films was inserts to db");
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Enable rollback transaction", ex);
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public void setReserve(Collection<Reserve> reserves) {
        try {
            getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(Constants.INSERT_RESERVE,
                    Statement.RETURN_GENERATED_KEYS);

            for (Reserve reserve : reserves) {
                preparedStatement.setInt(1, reserve.getShow_id());
                preparedStatement.setInt(2, reserve.getTicket_id());
                preparedStatement.setString(3, reserve.getVisitor_name());
                preparedStatement.setString(4, reserve.getTicket_code());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            if (LOG.isInfoEnabled())
                LOG.info("Reserve was inserts to db");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                int i = 0;
                List<Reserve> list = (List)reserves;
                while (generatedKeys.next()) {
                    list.get(i).setReserve_id(generatedKeys.getInt(1));
                    i++;
                }
            }

        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Enable rollback transaction", ex);
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public Boolean saveUser(User user) {
        try {
            getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.INSERT_USER);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            connection.commit();
            if (LOG.isInfoEnabled())
                LOG.info("Films was inserts to db");
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Enable rollback transaction", ex);
            }
            return false;
        } finally {
            closeConnection();
            return true;
        }
    }

    @Override
    public Boolean getUser(User user) {
        User result = null;
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_USER);
            preparedStatement.setString(1, user.getName());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new User(resultSet.getString("name"),
                        resultSet.getString("email"), resultSet.getString("pass"));
            }

            if (LOG.isInfoEnabled())
                LOG.info("Films selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            return false;
        } finally {
            Boolean compare = user.getPassword().equals(result.getPassword());
            return compare;
        }
    }

    @Override
    public void setTickets(Collection<Ticket> tickets) {
        try {
            getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(Constants.INSERT_TICKET,
                    Statement.RETURN_GENERATED_KEYS);

            for (Ticket ticket : tickets) {
                preparedStatement.setInt(1, ticket.getRoom_id());
                preparedStatement.setInt(2, ticket.getTicket_row());
                preparedStatement.setInt(3, ticket.getTicket_place());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            if (LOG.isInfoEnabled())
                LOG.info("Tickets was inserts to db");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                int i = 0;
                List<Ticket> list = (List)tickets;
                while (generatedKeys.next()) {
                    list.get(i).setTicket_id(generatedKeys.getInt(1));
                    i++;
                }
            }
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Enable rollback transaction", ex);
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public void setShow(Collection<Show> shows) {
        try {
            getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(Constants.INSERT_SHOW,
                    Statement.RETURN_GENERATED_KEYS);

            for (Show show : shows) {
                preparedStatement.setString(1, show.getFilm_nameCinema());
                preparedStatement.setInt(2, show.getRoom_id());
                Object param = new java.sql.Timestamp(show.getShow_time().getTime());
                preparedStatement.setObject(3, param);
                preparedStatement.setString(4, show.getFilm_name());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            if (LOG.isInfoEnabled())
                LOG.info("Shows was inserts to db");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                int i = 0;
                List<Show> list = (List)shows;
                while (generatedKeys.next()) {
                    list.get(i).setShow_id(generatedKeys.getInt(1));
                    i++;
                }
            }
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.error("Enable rollback transaction", ex);
            }
        } finally {
            closeConnection();
        }
    }

    @Override
    public void clean(String truncate, String resetIncrement) {
        try {
            getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            statement.addBatch(Constants.CHECK_FOREIGN_KEY_OFF);
            statement.addBatch(truncate);
            statement.addBatch(resetIncrement);
            statement.addBatch(Constants.CHECK_FOREIGN_KEY_ON);

            statement.executeBatch();
            connection.commit();

            if (LOG.isInfoEnabled())
                LOG.info("Table was truncated");
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            closeConnection();
        }
    }

    private boolean checkExecute(int[] count) {
        boolean ok = true;
        for (int i : count)
            if (i != 0)
                ok = !ok;
        return ok;
    }

    @Override
    public Collection<Film> getFilms() {
        Collection<Film> array = new ArrayList<>();
        try {
            getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(Constants.GET_FILMS);

            fillFilms(array, resultSet);

            if (LOG.isInfoEnabled())
                LOG.info("Films selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }
    private void fillFilms(Collection<Film> array, ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            array.add(new Film(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getInt(4), resultSet.getDouble(5), resultSet.getBoolean(6),
                    resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                    resultSet.getString(10), resultSet.getString(11), resultSet.getString(12),
                    resultSet.getString(13), resultSet.getString(14), resultSet.getString(15),
                    resultSet.getString(16), (java.util.Date) resultSet.getObject(17), resultSet.getString(18)));
        }
    }

    @Override
    public int getMaxIdRoom() {
        int maxId = -1;
        try {
            getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(Constants.GET_MAX_ID_ROOM);

            if (resultSet.next())
                maxId = resultSet.getInt("max_id");

            if (LOG.isInfoEnabled())
                LOG.info("Rooms was selected");

            closeConnection();
        }catch (SQLException e){
            LOG.error("Enable execute statement", e);
        }finally {
            return maxId;
        }
    }

    @Override
    public Room getMaxPositionOfRoom(int room_id) {
        Room room = null;
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_MAX_POSITION_ROOM);
            preparedStatement.setInt(1, room_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                room = new Room(resultSet.getInt("Room_id"), resultSet.getFloat("Room_vip_coef"),
                        resultSet.getFloat("Room_not_vip_coef"), resultSet.getInt("max_row"), resultSet.getInt("max_place"));
            }

            if (LOG.isInfoEnabled())
                LOG.info("Room selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return room;
        }
    }

    @Override
    public Collection<Show> getShowsForFilmCinema(String filmName, String cinemaName) {
        List<Show> array = new ArrayList<>();
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_SHOWS_FOR_FILMCINEMA);
            preparedStatement.setString(1, cinemaName);
            preparedStatement.setString(2, filmName);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Show show = new Show();
                show.setShow_id(resultSet.getInt("Show_id"));
                show.setFilm_nameCinema(resultSet.getString("Film_nameCinema"));
                show.setRoom_id(resultSet.getInt("Room_id"));

                Timestamp timestamp = resultSet.getTimestamp("Show_time");
                java.util.Date date = new java.util.Date(timestamp.getTime());
                show.setShow_time(date);
                show.setFilm_name(resultSet.getString("Film_name"));

                array.add(show);
            }

            if (LOG.isInfoEnabled())
                LOG.info("Shows selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }

    @Override
    public Collection<Room> getRooms() {
        Collection<Room> array = new ArrayList<>();
        try {
            getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(Constants.GET_ROOMS);

            while (resultSet.next()) {
                Room room = new Room(resultSet.getInt("Room_id"),
                        resultSet.getFloat("Room_vip_coef"), resultSet.getFloat("Room_not_vip_coef"),
                        resultSet.getInt("max_row"), resultSet.getInt("max_place"));
                array.add(room);
            }

            if (LOG.isInfoEnabled())
                LOG.info("Rooms was selected");

            closeConnection();
        }catch (SQLException e){
            LOG.error("Enable execute statement", e);
        }finally {
            return array;
        }
    }

    @Override
    public Collection<Film> getCinemas() {
        Collection<Film> array = new ArrayList<>();
        try {
            getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(Constants.GET_CINEMAS);

            fillCinemas(resultSet, array);

            if (LOG.isInfoEnabled())
                LOG.info("Cinemas selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }

    @Override
    public Collection<String> getCinemaNames() {
        Collection<String> array = new ArrayList<>();
        try {
            getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(Constants.GET_CINEMAS_NAME);

            while (resultSet.next()) {
                array.add(resultSet.getString(1));
            }

            if (LOG.isInfoEnabled())
                LOG.info("Cinema names selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }

    @Override
    public Collection<Film> getCinemasByFilm(String filmName) {
        Collection<Film> array = new ArrayList<>();
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_CINEMAS_BY_FILM);
            preparedStatement.setString(1, filmName);

            ResultSet resultSet = preparedStatement.executeQuery();
            fillCinemas(resultSet, array);

            if (LOG.isInfoEnabled())
                LOG.info("Cinemas selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }
    private void fillCinemas(ResultSet resultSet, Collection<Film> array) throws SQLException {
        while(resultSet.next()) {
            Film film = new Film();
            film.setFilm_nameCinema(resultSet.getString("Film_nameCinema"));
            film.setFilm_addressCinema(resultSet.getString("Film_addressCinema"));
            film.setFilm_phone(resultSet.getString("Film_phone"));
            film.setFilm_subway(resultSet.getString("Film_subway"));
            film.setFilm_imageCinema(resultSet.getString("Film_imageCinema"));

            array.add(film);
        }
    }

    @Override
    public Collection<Film> getFilmsFromShowsByCinemaName(String cinemaName) {
        Collection<Film> array = new ArrayList<>();
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.FILMS_BY_CINEMANAME_SHOWS);
            preparedStatement.setString(1, cinemaName);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Film film = new Film();
                film.setFilm_name(resultSet.getString("Film_name"));
                film.setFilm_nameCinema(resultSet.getString("Film_nameCinema"));
                Object o = resultSet.getObject("Film_runningTime");
                film.setFilm_runningTime((java.util.Date) o);
                film.setFilm_description(resultSet.getString("Film_description"));

                array.add(film);
            }

            if (LOG.isInfoEnabled())
                LOG.info("Cinemas selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }

    @Override
    public Integer getShowId(String filmName, String cinemaName, int room_id) {
        Integer id = -1;
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_SHOW_ID);
            preparedStatement.setString(1, filmName);
            preparedStatement.setString(2, cinemaName);
            preparedStatement.setInt(3, room_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            if (LOG.isInfoEnabled())
                LOG.info("Show id selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return id;
        }
    }

    @Override
    public Collection<Reserve> getReserveByShowId(int showId) {
        Collection<Reserve> result = new ArrayList<>();
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_RESERVE);
            preparedStatement.setInt(1, showId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reserve reserve = new Reserve(resultSet.getInt("Reserve_id"),
                        resultSet.getInt("Show_id"),
                        resultSet.getInt("Ticket_id"),
                        resultSet.getString("Visitor_name"),
                        resultSet.getString("Ticket_code"));
                result.add(reserve);
            }

            if (LOG.isInfoEnabled())
                LOG.info("Show id selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return result;
        }
    }

    @Override
    public Collection<Ticket> getTicketsById(Collection<Integer> ids) {
        Collection<Ticket> array = new ArrayList<>();
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.GET_TICKET_BY_ID);

            for (Integer id : ids) {
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Ticket ticket = new Ticket(resultSet.getInt("Ticket_id"),
                            resultSet.getInt("Room_id"),
                            resultSet.getInt("Ticket_row"),
                            resultSet.getInt("Ticket_place"));

                    array.add(ticket);
                }
            }

            if (LOG.isInfoEnabled())
                LOG.info("Cinemas selected");

            closeConnection();
        } catch (SQLException e) {
            LOG.error("Enable execute statement", e);
        } finally {
            return array;
        }
    }
}
