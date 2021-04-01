package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class RoueDeSecours extends Parade {
    public RoueDeSecours() {
        super("Roue de Secours");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParRoueDeSecours();
    }
}
