package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Niveau;

public class NiveauService {

    private final static Logger LOGGER = Logger.getLogger(NiveauService.class.getName());

    private final static String GET = "select * from niveau where id=?";
    private final static String GET_ALL = "select * from niveau";

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

    public static List<Niveau> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Niveau> ret = new ArrayList<Niveau>();
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

    private static Niveau convert(ResultSet res) throws SQLException {
        Niveau niveau = new Niveau();
        niveau.setId(res.getInt("id"));
        niveau.setNom(res.getString("nom"));
        return niveau;
    }
}
