package mille_bornes.cartes.attaques;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;

public class LimiteVitesse extends Attaque {
    public LimiteVitesse() {
        super("Limite de vitesse");
    }

    @Override
    public boolean estContreeParFinDeLimite() {
        return true;
    }

    @Override
    public boolean estContreeParVehiculePrioritaire() {
        return true;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        if(joueur.getLimiteVitesse()) {
            throw new IllegalStateException("Le joueur a déjà une limite de vitesse!");
        }
        joueur.setLimiteVitesse(true);
    }
}
