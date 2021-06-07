package mille_bornes.modele.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mille_bornes.modele.Jeu;

import java.io.*;

public class JsonUtils {
    public static boolean verifieExiste(JsonObject jsonObject, String... proprietes) {
        for (String prop : proprietes) {
            if (!jsonObject.has(prop)) return false;
        }
        return true;
    }

    public static Jeu chargerJeuDepuisFichier(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader lecteur = new FileReader(file);
        JsonObject json = gson.fromJson(lecteur, JsonObject.class);
        return new Jeu(json);
    }

    public static void sauvegarderJeuDansFichier(Jeu jeu, File file) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(file);
        gson.toJson(jeu.sauvegarder(), writer);
        writer.flush();
        writer.close();
    }
}
