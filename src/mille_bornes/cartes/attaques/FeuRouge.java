package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class FeuRouge extends Attaque {
    private static final long serialVersionUID = -3635645864264290565L;

    public FeuRouge() {
        super("Feu rouge");
    }

    @Override
    public boolean estContreeParVehiculePrioritaire() {
        return true;
    }

    @Override
    public boolean estContreeParFeuVert() {
        return true;
    }
}
