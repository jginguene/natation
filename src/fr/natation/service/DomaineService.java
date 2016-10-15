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

    private final static String GET = "select * from type_aptitude where id=?";
    private final static String GET_ALL = "select * from type_aptitude ";

    public static Domaine get(int typeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, typeId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + typeId + ") failed", e);
        } finally {
            connection.close();
        }

        throw new Exception("Il n'existe pas de niveau avec l'id " + typeId);
    }

    public static List<Domaine> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Domaine> ret = new ArrayList<Domaine>();
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

    private static Domaine convert(ResultSet res) throws SQLException {
        Domaine type = new Domaine();
        type.setId(res.getInt("id"));
        type.setNom(res.getString("nom"));
        return type;
    }

}
