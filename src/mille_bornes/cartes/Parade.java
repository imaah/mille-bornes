package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Parade extends Bataille {
    public Parade(String nom) {
        super(nom, Categorie.PARADE);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        Bataille bataille = joueur.getBataille();

        if(bataille instanceof Attaque) {
            if(contre((Attaque) bataille)) {
                joueur.defausseBataille(jeu);
            }
        }
    }
}
