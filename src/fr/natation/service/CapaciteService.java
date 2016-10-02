package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Capacite;

public class CapaciteService {

    private final static Logger LOGGER = Logger.getLogger(CapaciteService.class.getName());

    private final static String GET = "select * from capacite where id=?";

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

    private static Capacite convert(ResultSet res) throws SQLException {
        Capacite capacite = new Capacite();
        capacite.setId(res.getInt("id"));
        capacite.setNom(res.getString("nom"));
        return capacite;
    }
}
