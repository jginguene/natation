package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.Utils;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;

public class EleveService {

    private static String INSERT = "insert into Eleve (Nom, Prenom, groupe_id, classe_id) values (?,?,?,?)";
    private static String UPDATE = "update Eleve set Nom=?, Prenom=? , groupe_id=?, classe_id=? where  id = ?";
    private static String DELETE = "delete from Eleve  where  id = ?";
    private static String GET_ALL = "select * from Eleve";
    private static String GET_ALL_GROUPE = "select * from Eleve where groupe_id = ?";
    private static String GET = "select * from Eleve where id=?";
    private static String LAST_ID = "select last_insert_rowid()";
    private static String GET_NB_ELEVE = "select count(*) from eleve  where groupe_id = ? ";

    private final static Logger LOGGER = Logger.getLogger(EleveService.class.getName());

    private static int count;

    private static List<Eleve> eleves = new ArrayList<>();

    public static int getNbEleve(int groupeId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_NB_ELEVE);
            statement.setInt(1, groupeId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getInt(1);
            }
            return -1;
        } catch (Exception e) {
            throw new Exception("getNbEleve(" + groupeId + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static void delete(int eleveId) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setInt(1, eleveId);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("delete(" + eleveId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static int create(Eleve eleve) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, eleve.getNom().trim().toLowerCase());
            statement.setString(2, eleve.getPrenom().trim().toLowerCase());
            statement.setInt(3, eleve.getGroupeId());
            statement.setInt(4, eleve.getClasseId());
            statement.execute();

            statement = connection.prepareStatement(LAST_ID);
            ResultSet res = statement.executeQuery();
            return res.getInt(1);

        } catch (Exception e) {
            throw new Exception("create(" + eleve + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Eleve> getAll() throws Exception {

        if (!eleves.isEmpty()) {
            return new ArrayList<>(eleves);
        }

        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Eleve> ret = new ArrayList<Eleve>();
            while (res.next()) {
                ret.add(convert(res));
            }

            eleves.addAll(ret);
            return eleves;
        } catch (Exception e) {
            throw new Exception("getAll() failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Eleve> getAll(Groupe groupe) throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL_GROUPE);
            statement.setInt(1, groupe.getId());
            ResultSet res = statement.executeQuery();

            List<Eleve> ret = new ArrayList<Eleve>();
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

    public static Eleve get(int eleveId) throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, eleveId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Exception("get(" + eleveId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static void update(Eleve eleve) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, eleve.getNom());
            statement.setString(2, eleve.getPrenom());
            statement.setInt(3, eleve.getGroupeId());
            statement.setInt(4, eleve.getClasseId());
            statement.setInt(5, eleve.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("update(" + eleve + ") failed", e);
        } finally {
            connection.close();
        }
    }

    private static Eleve convert(ResultSet res) throws SQLException {
        Eleve eleve = new Eleve();
        eleve.setId(res.getInt("id"));
        eleve.setNom(Utils.capitalize(res.getString("Nom")));
        eleve.setPrenom(Utils.capitalize(res.getString("Prenom")));
        eleve.setGroupeId(res.getInt("Groupe_Id"));
        eleve.setClasseId(res.getInt("classe_Id"));
        return eleve;

    }

    public static void clearCache() {
        eleves.clear();
    }
}
