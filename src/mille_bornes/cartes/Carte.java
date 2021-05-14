package mille_bornes.cartes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.extensions.sauvegarde.CarteAdapter;

import java.io.Serializable;
import java.util.Objects;

@JsonAdapter(CarteAdapter.class)
public abstract class Carte implements Serializable {
    private static final long serialVersionUID = -3871654206865696965L;

    @Expose
    public final String nom;
    @Expose
    public final Categorie categorie;

    public Carte(String nom, Categorie categorie) {
        this.nom = nom;
        this.categorie = categorie;
    }

    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException;

    @Override
    public String toString() {
        return this.nom;
    }

    @Override
    public boolean equals(Object o) {
        if (!getClass().isAssignableFrom(o.getClass())) return false;
        Carte carte = (Carte) o;
        return Objects.equals(nom, carte.nom) && categorie == carte.categorie;
    }
}
