package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Aptitude;

public class AptitudeService {

    private final static Logger LOGGER = Logger.getLogger(AptitudeService.class.getName());

    private final static String GET = "select * from aptitude where id=?";

    public static Aptitude get(int aptitudeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, aptitudeId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + aptitudeId + ") failed", e);
        } finally {
            connection.close();
        }
        throw new Exception("Il n'existe pas d'aptitude avec l'id " + aptitudeId);
    }

    private static Aptitude convert(ResultSet res) throws SQLException {
        Aptitude aptitude = new Aptitude();
        aptitude.setId(res.getInt("id"));
        aptitude.setDescription(res.getString("description"));
        return aptitude;
    }
}
