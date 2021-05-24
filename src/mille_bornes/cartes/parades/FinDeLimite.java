package mille_bornes.cartes.parades;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class FinDeLimite extends Parade {

    private static final long serialVersionUID = 885026341013870691L;

    public FinDeLimite() {
        super("Fin de limite", "assets/cartes/Fin_limite.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        // On ne peut enlever une limite de vitesse que si nous en avons une
        if(!joueur.getLimiteVitesse()) {
            throw new IllegalStateException("Vous n'avez pas de limite de vitesse!");
        }

        joueur.setLimiteVitesse(false);
    }
}