package mille_bornes.modele.cartes;

import mille_bornes.application.Asset;
import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;

public abstract class Botte extends Carte {
    public Botte(String nom, Asset image) {
        super(nom, Categorie.BOTTE, image);
    }

    public abstract boolean contre(Attaque carte);

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        joueur.addBotte(this);
    }

    @Override
    public String nomColore() {
        return "\u001B[32m" + nom + "\u001B[0m";
    }
}
