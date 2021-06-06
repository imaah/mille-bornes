package mille_bornes.modele.cartes.attaques;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;

public class PanneEssence extends Attaque {
    public PanneEssence() {
        super("Panne d'essence", Asset.CARTE_PANNE_D_ESSENCE);
    }

    @Override
    public boolean estContreeParEssence() {
        return true;
    }

    @Override
    public boolean estContreeParCiterne() {
        return true;
    }
}
