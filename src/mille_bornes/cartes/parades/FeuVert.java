package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class FeuVert extends Parade {
    private static final long serialVersionUID = 4173338254788305506L;

    public FeuVert() {
        super("Feu vert");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFeuVert();
    }
}
