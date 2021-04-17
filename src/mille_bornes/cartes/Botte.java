package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Botte extends Carte {
    public Botte(String nom) {
        super("Botte " + nom, Categorie.BOTTE);
    }

    public abstract boolean contre(Attaque carte);

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        joueur.addBotte(this);
    }
}
