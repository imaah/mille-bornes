package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class PanneEssence extends Attaque {
    public PanneEssence() {
        super("Panne d'essence");
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
