package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class Essence extends Parade {
    private static final long serialVersionUID = -2072905419430820175L;

    public Essence() {
        super("Essence", "assets/cartes/Essence.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParEssence();
    }
}
