package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Groupe;

public class GroupeService {

    private static String INSERT = "insert into Groupe (Nom, description) values (?,?)";
    private static String UPDATE = "update Groupe set Nom=?, description=? where  id = ?";
    private static String DELETE = "delete from Groupe  where  id = ?";
    private static String GET_ALL = "select * from groupe order by nom";
    private static String GET = "select * from groupe where id=?";
    private static String LAST_ID = "select last_insert_rowid()";

    private final static Logger LOGGER = Logger.getLogger(GroupeService.class.getName());

    private static List<Groupe> groupes = new ArrayList<>();

    public static void delete(int groupeId) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setInt(1, groupeId);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("delete(" + groupeId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static Groupe get(int groupeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, groupeId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                throw new Exception("Le groupe " + groupeId + " n 'existe pas");
            }
        } catch (Exception e) {
            throw new Exception("get() failed", e);
        } finally {
            connection.close();
        }
    }

    public static int create(Groupe groupe) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, groupe.getNom());
            statement.setString(2, groupe.getDescription());
            statement.execute();

            statement = connection.prepareStatement(LAST_ID);
            ResultSet res = statement.executeQuery();
            return res.getInt(1);
        } catch (Exception e) {
            throw new Exception("create(" + groupe + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Groupe> getAll() throws Exception {
        if (!groupes.isEmpty()) {
            return new ArrayList<>(groupes);
        }

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Groupe> ret = new ArrayList<Groupe>();
            while (res.next()) {
                ret.add(convert(res));
            }
            groupes.addAll(ret);
            return ret;
        } catch (Exception e) {
            throw new Exception("getAll() failed", e);
        } finally {
            connection.close();
        }
    }

    public static void update(Groupe groupe) throws Exception {
        groupes = null;
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, groupe.getNom());
            statement.setString(2, groupe.getDescription());
            statement.setInt(3, groupe.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("update(" + groupe + ") failed", e);
        } finally {
            connection.close();
        }
    }

    private static Groupe convert(ResultSet res) throws SQLException {
        Groupe groupe = new Groupe();
        groupe.setId(res.getInt("id"));
        groupe.setNom(res.getString("Nom"));
        groupe.setDescription(res.getString("Description"));
        return groupe;
    }

    public static void clearCache() {
        groupes.clear();
    }
}
