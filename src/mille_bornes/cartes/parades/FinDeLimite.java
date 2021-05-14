package mille_bornes.cartes.parades;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class FinDeLimite extends Parade {

    private static final long serialVersionUID = 885026341013870691L;

    public FinDeLimite() {
        super("Fin de limite");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        if(!joueur.getLimiteVitesse()) {
            throw new IllegalStateException("Vous n'avez pas de limite de vitesse!");
        }

        joueur.setLimiteVitesse(false);
    }
}