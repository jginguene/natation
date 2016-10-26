package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final static String GET_FOR_NIVEAU = "select * from competence where niveau_id = ?  order by niveau_id,domaine_id,num";
    private final static String GET_FOR_NIVEAU_DOMAINE = "select * from competence where niveau_id = ? and domaine_id = ? order by niveau_id,domaine_id,num";
    private final static String GET_FOR_ELEVE_NIVEAU_DOMAINE = "select * from competence  join eleve_competence_r on eleve_competence_r.competence_id = competence.id where eleve_id=? and niveau_id = ? and domaine_id = ? order by niveau_id,domaine_id,num";
    private final static String GET_FOR_ELEVE_NIVEAU = "select * from competence  join eleve_competence_r on eleve_competence_r.competence_id = competence.id where eleve_id=? and niveau_id = ? order by niveau_id,domaine_id,num";

    private final static String GET_FOR_ELEVE = "select * from competence  join eleve_competence_r on eleve_competence_r.competence_id = competence.id where eleve_id=?  order by num";

    private final static String GET_FOR_CAPACITE = "select * from competence join capacite_competence_r on competence.id = capacite_competence_r.competence_id where capacite_competence_r.capacite_id=?  order by niveau_id,domaine_id,num";

    private final static String LAST_ID = "select last_insert_rowid()";

    private static List<Competence> competences = new ArrayList<>();

    private static Map<String, List<Competence>> mapNiveauDomaine = new HashMap<>();
    private static Map<String, List<Competence>> mapNiveauDomaineEleve = new HashMap<>();

    public static void delete(int competenceId) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setInt(1, competenceId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("delete(" + competenceId + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Competence> getAll() throws Exception {
        if (!competences.isEmpty()) {
            return new ArrayList<>(competences);
        }

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
            while (res.next()) {
                ret.add(convert(res));
            }
            competences.addAll(ret);
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

    public static List<Competence> get(Niveau niveau, Domaine domaine) throws Exception {
        String mapKey = niveau.getId() + "#" + domaine.getId();
        if (mapNiveauDomaine.containsKey(mapKey)) {
            return new ArrayList<>(mapNiveauDomaine.get(mapKey));
        }

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_NIVEAU_DOMAINE);
            statement.setInt(1, niveau.getId());
            statement.setInt(2, domaine.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
            while (res.next()) {
                ret.add(convert(res));
            }

            mapNiveauDomaine.put(mapKey, new ArrayList<>(ret));
            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + niveau + ";" + domaine + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Competence> get(Niveau niveau) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_NIVEAU);
            statement.setInt(1, niveau.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();
            while (res.next()) {
                ret.add(convert(res));
            }
            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + niveau + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static List<Competence> get(Eleve eleve, Niveau niveau) throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_ELEVE_NIVEAU);
            statement.setInt(1, eleve.getId());
            statement.setInt(2, niveau.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();

            while (res.next()) {
                ret.add(convert(res));
            }

            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + eleve + "; " + niveau + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static List<Competence> get(Eleve eleve, Niveau niveau, Domaine domaine) throws Exception {
        String mapKey = eleve.getId() + "#" + niveau.getId() + "#" + domaine.getId();
        if (mapNiveauDomaineEleve.containsKey(mapKey)) {
            return new ArrayList<>(mapNiveauDomaineEleve.get(mapKey));
        }

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_ELEVE_NIVEAU_DOMAINE);
            statement.setInt(1, eleve.getId());
            statement.setInt(2, niveau.getId());
            statement.setInt(3, domaine.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();

            while (res.next()) {
                ret.add(convert(res));
            }

            mapNiveauDomaineEleve.put(mapKey, new ArrayList<>(ret));
            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + eleve + "; " + niveau + "; " + domaine + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static List<Competence> get(Eleve eleve) throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FOR_ELEVE);
            statement.setInt(1, eleve.getId());
            ResultSet res = statement.executeQuery();
            List<Competence> ret = new ArrayList<Competence>();

            while (res.next()) {
                ret.add(convert(res));
            }

            return ret;
        } catch (Exception e) {
            throw new Exception("get(" + eleve + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static int create(Competence competence) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, competence.getDescription());
            statement.setInt(2, competence.getNiveauId());
            statement.setInt(3, competence.getDomaineId());
            statement.execute();

            statement = connection.prepareStatement(LAST_ID);
            ResultSet res = statement.executeQuery();
            return res.getInt(1);
        } catch (Exception e) {
            throw new Exception("create(" + competence + ") failed", e);
        } finally {
            connection.close();
        }
    }

    public static Competence get(int competenceId) throws Exception {
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setInt(1, competenceId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            }
        } catch (Exception e) {
            throw new Exception("get(" + competenceId + ") failed", e);
        } finally {
            connection.close();
        }
        throw new Exception("Il n'existe pas d'competence avec l'id " + competenceId);
    }

    public static void add(Competence competence, Eleve eleve) throws Exception {
        clearCache();
        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_ELEVE);
            statement.setInt(1, eleve.getId());
            statement.setInt(2, competence.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("add(" + competence + "; " + eleve + ") failed", e);
        } finally {
            connection.close();
        }

    }

    public static void removeAll(Eleve eleve) throws Exception {
        clearCache();
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
        Competence competence = new Competence();
        competence.setId(res.getInt("id"));
        competence.setNiveauId(res.getInt("niveau_id"));
        competence.setDomaineId(res.getInt("domaine_id"));
        competence.setDescription(res.getString("description"));
        competence.setNum(res.getInt("num"));
        return competence;
    }

    private static void clearCache() {
        mapNiveauDomaine.clear();
        competences.clear();
    }

}
