package mille_bornes.modele.cartes.parades;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class Essence extends Parade {

    public Essence() {
        super("Essence", "images/cartes/Essence.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParEssence();
    }
}
