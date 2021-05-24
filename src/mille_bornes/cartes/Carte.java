package mille_bornes.cartes;

import com.google.gson.JsonObject;
import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.extensions.sauvegarde.Sauvegardable;

import java.io.Serializable;
import java.util.Objects;

public abstract class Carte implements Serializable {
    private static final long serialVersionUID = -3871654206865696965L;

    public static final Carte DEFAULT = new Carte("DEFAUT", Categorie.DEFAULT, "assets/cartes/Null.jpg") {
        @Override
        public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
            throw new IllegalStateException("Vous ne pouvez pas jouer une carte de Categorie DEFAULT!");
        }
    };

    public final String nom;
    public final Categorie categorie;
    public final String imagePath;

    public Carte(String nom, Categorie categorie, String imagePath) {
        this.nom = nom;
        this.categorie = categorie;

        imagePath = imagePath.trim();
        if(!imagePath.startsWith("/")) {
            imagePath = "/" + imagePath;
        }

        this.imagePath = getClass().getResource(imagePath).toString();
    }

    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException;

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
