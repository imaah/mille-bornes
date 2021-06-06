package mille_bornes.modele.cartes.parades;

import mille_bornes.application.Asset;
import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class FinDeLimite extends Parade {

    public FinDeLimite() {
        super("Fin de limite", Asset.CARTE_FIN_DE_LIMITE);
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        // On ne peut enlever une limite de vitesse que si nous en avons une
        if (!joueur.getLimiteVitesse()) {
            throw new IllegalStateException("Vous n'avez pas de limite de vitesse!");
        }

        joueur.setLimiteVitesse(false);
    }
}