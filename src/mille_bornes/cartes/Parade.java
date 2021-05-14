package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Parade extends Bataille {
    private static final long serialVersionUID = -1307910562516914625L;

    public Parade(String nom) {
        super(nom, Categorie.PARADE);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        Bataille bataille = joueur.getBataille();

        if(bataille instanceof Attaque) {
            if(contre((Attaque) bataille)) {
                joueur.defausseBataille(jeu);
            } else {
                throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
            }
        }
        joueur.setBataille(this);
    }
}
