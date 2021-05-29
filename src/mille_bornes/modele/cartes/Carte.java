package mille_bornes.modele.cartes;

import com.google.gson.JsonObject;
import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.extensions.sauvegarde.Sauvegardable;

import static mille_bornes.modele.utils.JsonUtils.verifieExiste;

import java.io.Serializable;
import java.util.Objects;

public abstract class Carte implements Serializable, Sauvegardable {
    private static final long serialVersionUID = -3871654206865696965L;
    public final String nom;
    public final Categorie categorie;
    private final String imagePath;

    public Carte(String nom, Categorie categorie, String imagePath) {
        this.nom = nom;
        this.categorie = categorie;

        imagePath = imagePath.trim();
        if (!imagePath.startsWith("/")) {
            imagePath = "/" + imagePath;
        }
        this.imagePath = imagePath;
    }

    public static Carte deserialize(JsonObject json) {
        try {
            if (verifieExiste(json, "class", "categorie", "imagePath")) {
                Class<?> clazz = Class.forName(json.get("class").getAsString());

                if (Carte.class.isAssignableFrom(clazz)) {

                    if (Borne.class.equals(clazz)) {

                        if (json.has("km")) {
                            return Borne.class
                                    .getConstructor(Integer.TYPE, String.class)
                                    .newInstance(json.get("km").getAsInt(), json.get("imagePath").getAsString());
                        }

                    } else {
                        Class<? extends Carte> carteClass = clazz.asSubclass(Carte.class);
                        Object obj = carteClass.getConstructor().newInstance();
                        return carteClass.cast(obj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Une erreur s'est produite lors de la déserialisation d'une carte.. (" + e.getMessage() + ")");
        }
        throw new IllegalArgumentException("Propriétés manquantes dans l'objet json.");
    }

    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException;

    public String getImagePath() {
        return getClass().getResource(imagePath).toString();
    }

    @Override
    public JsonObject sauvegarder() {
        JsonObject json = new JsonObject();

        json.addProperty("class", getClass().getName());
        json.addProperty("nom", nom);
        json.addProperty("categorie", categorie.name());
        json.addProperty("imagePath", imagePath);

        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (!getClass().isAssignableFrom(o.getClass())) return false;
        Carte carte = (Carte) o;
        return Objects.equals(nom, carte.nom) && categorie == carte.categorie;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
