package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Etape;

public class EtapeService {

    private final static Logger LOGGER = Logger.getLogger(EtapeService.class.getName());

    private final static String GET = "select * from etape_parcours where capacite_id=? order by num";

    public static List<Etape> get(int capaciteId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, capaciteId);
            ResultSet res = statement.executeQuery();
            List<Etape> etapes = new ArrayList<Etape>();
            while (res.next()) {
                etapes.add(convert(res));
            }
            return etapes;
        } catch (Exception e) {
            throw new Exception("get(" + capaciteId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    private static Etape convert(ResultSet res) throws SQLException {
        Etape etape = new Etape();
        etape.setId(res.getInt("id"));
        etape.setDescription(res.getString("description"));
        etape.setNum(res.getInt("num"));
        return etape;
    }

}
