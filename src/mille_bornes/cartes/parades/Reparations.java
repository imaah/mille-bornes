package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class Reparations extends Parade {
    private static final long serialVersionUID = 4135166053338020523L;

    public Reparations() {
        super("Reparations");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParReparations();
    }
}
