package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class Attaque extends Bataille {
    public Attaque(String nom) {
        super(nom, Categorie.ATTAQUE);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        super.appliqueEffet(jeu, joueur);
    }

    @Override
    public final boolean contre(Attaque attaque) {
        return false;
    }
}
