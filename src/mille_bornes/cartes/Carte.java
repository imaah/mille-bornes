package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

import java.util.Objects;

public abstract class Carte {
    public final String nom;
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
