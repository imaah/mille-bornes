package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

import java.io.Serializable;
import java.util.Objects;

public abstract class Carte implements Serializable {
    private static final long serialVersionUID = -3871654206865696965L;

    public final String nom;
    public final Categorie categorie;

    public Carte(String nom, Categorie categorie) {
        this.nom = nom;
        this.categorie = categorie;
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
