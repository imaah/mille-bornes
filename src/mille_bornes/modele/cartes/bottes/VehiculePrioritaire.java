package mille_bornes.modele.cartes.bottes;

import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class VehiculePrioritaire extends Botte {
    public VehiculePrioritaire() {
        super("Vehicule Prioritaire", "images/cartes/Prioritaire.jpg");
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
