package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Bataille extends Carte {

    private static final long serialVersionUID = 3250528684674187969L;

    public Bataille(String nom, Categorie categorie, String imagePath) {
        super(nom, categorie, imagePath);
    }

    public boolean estContreeParFeuVert() {
        return false;
    }

    public boolean estContreeParFinDeLimite() {
        return false;
    }

    public boolean estContreeParEssence() {
        return false;
    }

    public boolean estContreeParRoueDeSecours() {
        return false;
    }

    public boolean estContreeParReparations() {
        return false;
    }

    public boolean estContreeParVehiculePrioritaire() {
        return false;
    }

    public boolean estContreeParCiterne() {
        return false;
    }

    public boolean estContreeParIncrevable() {
        return false;
    }

    public boolean estContreeParAsDuVolant() {
        return false;
    }

    public abstract boolean contre(Attaque attaque);

    // Nous avons décidé de mettre cette méthode abstraite pour faciliter la création des autres classes
    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException;
}
