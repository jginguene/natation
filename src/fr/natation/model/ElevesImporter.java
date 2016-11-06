package fr.natation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import fr.natation.service.EleveService;

public class ElevesImporter {

    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public int importEleves(String fileName, Classe classe) throws Exception {

        File file = new File(fileName);

        if (!file.exists()) {
            throw new Exception("le fichier " + fileName + " na pas été trouvé");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file), "iso-8859-1"));

        String line = reader.readLine();
        int nbEleve = 0;
        while (line != null) {
            this.readLine(line, classe);
            line = reader.readLine();
            nbEleve++;
        }

        reader.close();
        return nbEleve;
    }

    private void readLine(String line, Classe classe) throws Exception {
        String[] arr = line.split(";");
        Eleve eleve = new Eleve();
        eleve.setNom(arr[0].trim());
        eleve.setPrenom(arr[1].trim());
        eleve.setDateDeNaissance(this.format.parse(arr[2].trim()));
        eleve.setClasseId(classe.getId());
        EleveService.create(eleve);

    }

}
