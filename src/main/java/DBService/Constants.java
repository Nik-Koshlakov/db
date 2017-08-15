package DBService;

/**
 * Created by Nik on 20.06.2017.
 */
public interface Constants {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/rsoi";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";

    public static final String CHECK_FOREIGN_KEY_OFF = "SET FOREIGN_KEY_CHECKS = 0";
    public static final String CHECK_FOREIGN_KEY_ON = "SET FOREIGN_KEY_CHECKS = 1";

    public static final String TRUNCATE_FILMS = "truncate table films";
    public static final String RESET_AUTOINCREMENT_FILMS = "ALTER TABLE films AUTO_INCREMENT = 1";

    public static final String TRUNCATE_RESERVE = "truncate table reserve";
    public static final String RESET_AUTOINCREMENT_RESERVE = "ALTER TABLE reserve AUTO_INCREMENT = 1";

    public static final String TRUNCATE_SHOWS = "truncate table shows";
    public static final String RESET_AUTOINCREMENT_SHOWS = "ALTER TABLE shows AUTO_INCREMENT = 1";

    public static final String TRUNCATE_TICKETS = "truncate table tickets";
    public static final String RESET_AUTOINCREMENT_TICKETS = "ALTER TABLE tickets AUTO_INCREMENT = 1";

    public static final String INSERT_FILM = "INSERT Films (Film_name, Film_image, Film_length, Film_price, Film_imax, Film_country, " +
            "Film_year, Film_trailer, Film_age, Film_derictor, Film_nameCinema, Film_addressCinema, Film_phone, " +
            "Film_subway, Film_imageCinema, Film_runningTime, Film_description)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String INSERT_RESERVE = "insert reserve (Show_id, Ticket_id, Visitor_name, Ticket_code)\n" +
            "values (?, ?, ?, ?)";

    public static final String INSERT_SHOW = "insert shows (Film_nameCinema, Room_id, Show_time, Film_name) " +
            "values (?, ?, ?, ?)";

    public static final String INSERT_TICKET = "insert tickets (Room_id, Ticket_row, Ticket_place)" +
            "values (?, ?, ?)";

    public static final String INSERT_USER = "insert users (name, email, pass) values (?, ?, ?)";
    public static final String GET_USER = "select * from users where name like ?";

    public static final String GET_FILMS = "select * from films group by Film_name";
    public static final String GET_ROOMS = "select * from rooms";
    public static final String GET_RESERVE = "select *\n" +
            "from reserve\n" +
            "where Show_id = ?";
    public static final String GET_TICKET_BY_ID = "select * from tickets where ticket_id = ?";
    public static final String GET_MAX_ID_ROOM = "select max(Room_id) as max_id from rooms";
    public static final String GET_MAX_POSITION_ROOM = "select * from rooms where room_id = ?";
    public static final String GET_SHOWS_FOR_FILMCINEMA = "select * \n" +
            "from shows \n" +
            "where Film_nameCinema like ? and Film_name like ? \n" +
            "order by Show_time";

    public static final String GET_CINEMAS_NAME = "select Film_nameCinema from films group by Film_nameCinema";
    public static final String GET_CINEMAS = "select Film_nameCinema, Film_addressCinema, Film_phone, Film_subway, Film_imageCinema\n" +
            "from films\n" +
            "group by Film_nameCinema\n" +
            "order by Film_nameCinema";
    public static final String GET_CINEMAS_BY_FILM = "select Film_nameCinema, Film_addressCinema, Film_phone, Film_subway, Film_imageCinema\n" +
            "from films\n" +
            "where Film_name like ?";
    public static final String GET_SHOW_ID = "select Show_id\n"+
            "from shows\n"+
            "where Film_name like ?\n"+
            "\tand Film_nameCinema like ?\n"+
            "    and Room_id = ?";

    public static final String FILMS_BY_CINEMANAME_SHOWS = "select Film_name, Film_nameCinema, Film_runningTime, Film_description\n" +
            "from films\n" +
            "where Film_name in (select Film_name\n" +
            "\t\t\t\t\tfrom films\n" +
            "\t\t\t\t\tgroup by Film_name) \n" +
            "and Film_nameCinema like ?\n" +
            "order by Film_nameCinema, Film_runningTime;";


}
