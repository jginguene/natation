package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;

public class CompetenceService {

    private final static Logger LOGGER = Logger.getLogger(CompetenceService.class.getName());

    private final static String INSERT = "insert into competence (description, niveau_id, domaine_id) values (?,?,?)";
    private final static String INSERT_ELEVE = "insert into eleve_competence_r (eleve_id, competence_id) values (?,?)";

    private final static String REMOVE_ELEVE = "delete from eleve_competence_r where eleve_id=?";

    private final static String GET = "select * from competence where id=?";
    private final static String DELETE = "delete from competence  where  id = ?";
    private final static String GET_ALL = "select * from competence order by niveau_id,domaine_id,num";

    private final static String GET_FOR_NIVEAU_TYPE = "select * from competence where niveau_id = ? and domaine_id = ? order by niveau_id,domaine_id,num";
    private final static String GET_FOR_ELEVE_NIVEAU_TYPE = "select * from competence  join eleve_competence_r on eleve_competence_r.competence_id = competence.id where eleve_id=? and niveau_id = ? and domaine_id = ? order by niveau_id,domaine_id,num";

    private final static String GET_FOR_CAPACITE = "select * from aptitude join capacite_competence_r on competence.id = capacite_competence_r.competence_id where capacite_competence_r.capacite_id=?  order by niveau_id,domaine_id,num";

    private final static String LAST_ID = "select last_insert_rowid()";

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

    public static List<Competence> getAll() throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
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

    public static List<Competence> get(Capacite capacite) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_CAPACITE);
            statement.setInt(1, capacite.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
            while (res.next()) {
                ret.add(convert(res));
            }
            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + capacite + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Competence> get(Niveau niveau, Domaine type) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_NIVEAU_TYPE);
            statement.setInt(1, niveau.getId());
            statement.setInt(2, type.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
            while (res.next()) {
                ret.add(convert(res));
            }
            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + niveau + ";" + type + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static Competence get(Eleve eleve, Niveau niveau, Domaine type) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_ELEVE_NIVEAU_TYPE);
            statement.setInt(1, eleve.getId());
            statement.setInt(2, niveau.getId());
            statement.setInt(3, type.getId());
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Exception("get(" + eleve + "; " + niveau + "; " + type + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static int create(Competence aptitude) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, aptitude.getDescription());
            statement.setInt(2, aptitude.getNiveauId());
            statement.setInt(3, aptitude.getDomaineId());
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

    public static Competence get(int aptitudeId) throws Exception {
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

    public static void add(Competence aptitude, Eleve eleve) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_ELEVE);
            statement.setInt(1, eleve.getId());
            statement.setInt(2, aptitude.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("add(" + aptitude + "; " + eleve + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static void removeAll(Eleve eleve) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(REMOVE_ELEVE);
            statement.setInt(1, eleve.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("remove(" + eleve + ") failed", e);
        } finally {
            connection.close();
        }

    }

    private static Competence convert(ResultSet res) throws SQLException {
        Competence aptitude = new Competence();
        aptitude.setId(res.getInt("id"));
        aptitude.setNiveauId(res.getInt("niveau_id"));
        aptitude.setDomaineId(res.getInt("domaine_id"));
        aptitude.setDescription(res.getString("description"));
        aptitude.setNum(res.getInt("num"));
        return aptitude;
    }

}
