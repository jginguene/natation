package fr.natation.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fr.natation.ConnectionFactory;
import fr.natation.Utils;
import fr.natation.model.Ecole;

public class EcoleService {

    private static String UPDATE = "update ecole set Nom=?, adresse=? , code_postal=?, ville=? where  id = 1";
    private static String GET = "select * from Ecole where id=1";

    private final static Logger LOGGER = Logger.getLogger(EcoleService.class.getName());

    public static Ecole get() throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return convert(res);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Exception("get() failed", e);
        } finally {
            connection.close();
        }
    }

    public static void update(Ecole ecole) throws Exception {

        Connection connection = ConnectionFactory.createConnection();
        try {

            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, ecole.getNom());
            statement.setString(2, ecole.getAdresse());
            statement.setString(3, ecole.getCodePostal());
            statement.setString(4, ecole.getVille());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("update() failed", e);
        } finally {
            connection.close();
        }
    }

    private static Ecole convert(ResultSet res) throws SQLException {
        Ecole ecole = new Ecole();
        ecole.setId(res.getInt("id"));
        ecole.setNom(Utils.capitalize(res.getString("Nom")));
        ecole.setAdresse(res.getString("Adresse"));
        ecole.setCodePostal(res.getString("Code_Postal"));
        ecole.setVille(res.getString("Ville"));
        return ecole;

    }

}
