package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Niveau;

public class NiveauService {

    private final static Logger LOGGER = Logger.getLogger(NiveauService.class.getName());

    private final static String GET = "select * from niveau where id=?";

    public static Niveau get(int niveauId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, niveauId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + niveauId + ") failed", e);
        } finally {
            connection.close();
        }

        throw new Exception("Il n'existe pas de niveau avec l'id " + niveauId);
    }

    private static Niveau convert(ResultSet res) throws SQLException {
        Niveau niveau = new Niveau();
        niveau.setId(res.getInt("id"));
        niveau.setNom(res.getString("nom"));
        return niveau;
    }
}
