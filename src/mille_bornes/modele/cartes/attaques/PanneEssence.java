package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class PanneEssence extends Attaque {
    public PanneEssence() {
        super("Panne d'essence", "images/cartes/Panne_Essence.jpg");
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
