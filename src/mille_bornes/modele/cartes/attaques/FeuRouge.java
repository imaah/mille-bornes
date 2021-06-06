package mille_bornes.modele.cartes.attaques;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;

public class FeuRouge extends Attaque {
    public FeuRouge() {
        super("Feu rouge", Asset.CARTE_FEU_ROUGE);
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
