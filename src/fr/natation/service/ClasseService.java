package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Classe;

public class ClasseService {

    private static String GET_ALL = "select * from classe order by id";
    private static String GET = "select * from classe where id=?";
    private static String LAST_ID = "select last_insert_rowid()";

    private final static Logger LOGGER = Logger.getLogger(ClasseService.class.getName());

    public static Classe get(int classeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, classeId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                throw new Exception("La classe " + classeId + " n 'existe pas");
            }
        } catch (Exception e) {
            throw new Exception("get() failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Classe> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Classe> ret = new ArrayList<Classe>();
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

    private static Classe convert(ResultSet res) throws SQLException {
        Classe classe = new Classe();
        classe.setId(res.getInt("id"));
        classe.setNom(res.getString("Nom"));
        classe.setNiveauId(res.getInt("niveau_id"));
        return classe;

    }
}
