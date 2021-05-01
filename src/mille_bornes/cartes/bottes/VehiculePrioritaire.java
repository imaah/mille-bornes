package mille_bornes.cartes.bottes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class VehiculePrioritaire extends Botte {
    public VehiculePrioritaire() {
        super("Vehicule Prioritaire");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParVehiculePrioritaire();
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        super.appliqueEffet(jeu, joueur);
        joueur.setLimiteVitesse(false);
    }
}
