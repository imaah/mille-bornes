package mille_bornes.modele.extensions.sauvegarde;

import com.google.gson.JsonObject;

public interface Sauvegardable {
    JsonObject sauvegarder();
}
