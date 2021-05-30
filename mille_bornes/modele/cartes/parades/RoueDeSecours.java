package mille_bornes.modele.cartes.parades;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class RoueDeSecours extends Parade {
    private static final long serialVersionUID = 1871251187248523728L;

    public RoueDeSecours() {
        super("Roue de Secours", "assets/cartes/Secours.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParRoueDeSecours();
    }
}
