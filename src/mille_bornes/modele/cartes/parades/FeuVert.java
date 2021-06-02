package mille_bornes.modele.cartes.parades;

import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Bataille;
import mille_bornes.modele.cartes.Parade;

public class FeuVert extends Parade {

    public FeuVert() {
        super("Feu vert", "images/cartes/Roulez.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        Bataille bataille = joueur.getBataille();

        // Si la carte du haut de la bataille est une attaque...
        if (bataille instanceof Attaque) {
            // ... on vérifie que notre carte la contre bien
            if (contre((Attaque) bataille)) {
                joueur.defausseBataille(jeu);
                joueur.setBataille(this);
            } else {
                // Sinon on ne peut pas la jouer
                throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
            }
        } else if (bataille == null) {
            joueur.setBataille(this);
        } else {
            throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
        }
    }
}
