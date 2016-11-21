package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Domaine;

public class DomaineService {

    private final static Logger LOGGER = Logger.getLogger(DomaineService.class.getName());

    private final static String GET = "select * from domaine where id=?";
    private final static String GET_ALL = "select * from domaine ";

    private static List<Domaine> domaines = new ArrayList<>();

    public static Domaine get(int domaineId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, domaineId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + domaineId + ") failed", e);
        } finally {
            connection.close();
        }

        throw new Exception("Il n'existe pas de domaine avec l'id " + domaineId);
    }

    public static List<Domaine> getAll() throws Exception {
        if (!domaines.isEmpty()) {
            return new ArrayList<>(domaines);
        }

        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Domaine> ret = new ArrayList<Domaine>();
            while (res.next()) {
                ret.add(convert(res));
            }
            domaines.addAll(ret);
            return ret;
        } catch (Exception e) {
            throw new Exception("getAll() failed", e);
        } finally {
            connection.close();
        }
    }

    private static Domaine convert(ResultSet res) throws SQLException {
        Domaine domaine = new Domaine();
        domaine.setId(res.getInt("id"));
        domaine.setNom(res.getString("nom"));
        domaine.setUrl(res.getString("url"));
        return domaine;
    }

}
