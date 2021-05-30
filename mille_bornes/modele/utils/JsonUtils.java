package mille_bornes.modele.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mille_bornes.modele.Jeu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonUtils {
    public static boolean verifieExiste(JsonObject jsonObject, String... proprietes) {
        for(String prop : proprietes) {
            if(!jsonObject.has(prop)) return false;
        }
        return true;
    }

    public static Jeu chargerJeuDepuisFichier(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader lecteur = new FileReader(file);
        JsonObject json = gson.fromJson(lecteur, JsonObject.class);
        return new Jeu(json);
    }
}
