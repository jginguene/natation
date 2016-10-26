package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Assn;

public class AssnService {
    private static String GET_ALL = "select * from assn order by id";
    private static String GET = "select * from assn where id=?";
    private static String LAST_ID = "select last_insert_rowid()";

    private final static Logger LOGGER = Logger.getLogger(ClasseService.class.getName());

    public static Assn get(int assnId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, assnId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                throw new Exception("La assn " + assnId + " n 'existe pas");
            }
        } catch (Exception e) {
            throw new Exception("get() failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Assn> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Assn> ret = new ArrayList<Assn>();
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

    private static Assn convert(ResultSet res) throws SQLException {
        Assn assn = new Assn();
        assn.setId(res.getInt("id"));
        assn.setCode(res.getString("Code"));
        assn.setDescription(res.getString("description"));
        return assn;

    }
}
