package mille_bornes.cartes.parades;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Parade;

public class FeuVert extends Parade {
    private static final long serialVersionUID = 4173338254788305506L;

    public FeuVert() {
        super("Feu vert");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        Bataille bataille = joueur.getBataille();

        if (bataille instanceof Attaque) {
            if (contre((Attaque) bataille)) {
                joueur.defausseBataille(jeu);
            } else {
                throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
            }
        }

        joueur.setBataille(this);
    }
}
