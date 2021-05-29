package mille_bornes.modele.cartes;

import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;

public abstract class Botte extends Carte {
    private static final long serialVersionUID = -3439676551653430543L;

    public Botte(String nom, String imagePath) {
        super( "\u001B[32m" + nom + "\u001B[0m", Categorie.BOTTE, imagePath);
    }

    public abstract boolean contre(Attaque carte);

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        joueur.addBotte(this);
    }
}
