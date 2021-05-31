package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class FeuRouge extends Attaque {
    public FeuRouge() {
        super("Feu rouge", "images/cartes/Stop.jpg");
    }

    @Override
    public boolean estContreeParFeuVert() {
        return true;
    }

    @Override
    public boolean estContreeParVehiculePrioritaire() {
        return true;
    }
}
