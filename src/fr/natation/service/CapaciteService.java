package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Aptitude;
import fr.natation.model.Capacite;

public class CapaciteService {

    private final static Logger LOGGER = Logger.getLogger(CapaciteService.class.getName());

    private final static String GET = "select * from capacite where id=?";
    private final static String GET_ALL = "select * from capacite";
    private static String GET_FOR_APTITUDE = "select * from capacite join capacite_aptitude_r on capacite.id = capacite_aptitude_r.capacite_id where capacite_aptitude_r.aptitude_id=? ";

    public static Capacite get(int capaciteId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, capaciteId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + capaciteId + ") failed", e);
        } finally {
            connection.close();
        }
        throw new Exception("Il n'existe pas de capactié avec l'id " + capaciteId);
    }

    public static Capacite get(Aptitude aptitude) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_APTITUDE);
            statement.setInt(1, aptitude.getId());
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Exception("get(" + aptitude + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Capacite> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Capacite> ret = new ArrayList<Capacite>();
            while (res.next()) {
                ret.add(convert(res));
            }
            return ret;
        } catch (Exception e) {
            throw new Exception("getAll() failed", e);
        } finally {
            connection.close();
        }
    }

    private static Capacite convert(ResultSet res) throws SQLException {
        Capacite capacite = new Capacite();
        capacite.setId(res.getInt("id"));
        capacite.setNom(res.getString("nom"));
        return capacite;
    }
}
