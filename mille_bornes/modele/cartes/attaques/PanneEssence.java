package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class PanneEssence extends Attaque {
    private static final long serialVersionUID = 1840603587728231063L;

    public PanneEssence() {
        super("Panne d'essence", "assets/cartes/Panne_Essence.jpg");
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
