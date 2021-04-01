package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class Essence extends Parade {
    public Essence() {
        super("Essence");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParEssence();
    }
}
