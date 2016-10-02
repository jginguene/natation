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

public class AptitudeService {

    private final static Logger LOGGER = Logger.getLogger(AptitudeService.class.getName());

    private static String INSERT = "insert into aptitude (description, niveau_id, type_id,capacite_id) values (?,?,?,?)";
    private static String UPDATE = "update aptitude set description=?, niveau_id=? , type_id=?, capacite_id=?  where  id = ?";
    private final static String GET = "select * from aptitude where id=?";
    private static String DELETE = "delete from aptitude  where  id = ?";
    private static String GET_ALL = "select * from aptitude";
    private static String LAST_ID = "select last_insert_rowid()";

    public static void delete(int aptitudeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setInt(1, aptitudeId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("delete(" + aptitudeId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Aptitude> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Aptitude> ret = new ArrayList<Aptitude>();
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

    public static int create(Aptitude aptitude) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, aptitude.getDescription());
            statement.setInt(2, aptitude.getNiveauId());
            statement.setInt(3, aptitude.getTypeId());
            statement.setInt(4, aptitude.getCapaciteId());
            statement.execute();

            statement = connection.prepareStatement(LAST_ID);
            ResultSet res = statement.executeQuery();
            return res.getInt(1);

        } catch (Exception e) {
            throw new Exception("create(" + aptitude + ") failed", e);
        } finally {
            connection.close();
        }
    }

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

    public static void update(Aptitude aptitude) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, aptitude.getDescription());
            statement.setInt(2, aptitude.getNiveauId());
            statement.setInt(3, aptitude.getTypeId());
            statement.setInt(4, aptitude.getCapaciteId());
            statement.setInt(5, aptitude.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("update(" + aptitude + ") failed", e);
        } finally {
            connection.close();
        }
    }

    private static Aptitude convert(ResultSet res) throws SQLException {
        Aptitude aptitude = new Aptitude();
        aptitude.setId(res.getInt("id"));
        aptitude.setNiveauId(res.getInt("niveau_id"));
        aptitude.setTypeId(res.getInt("type_id"));
        aptitude.setCapaciteId(res.getInt("capacite_id"));
        aptitude.setDescription(res.getString("description"));
        return aptitude;
    }
}
