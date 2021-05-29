package mille_bornes.modele.utils;

import com.google.gson.JsonObject;

public class JsonUtils {
    public static boolean verifieExiste(JsonObject jsonObject, String... proprietes) {
        for(String prop : proprietes) {
            if(!jsonObject.has(prop)) return false;
        }
        return true;
    }
}
