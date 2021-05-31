package mille_bornes.modele.cartes.parades;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class RoueDeSecours extends Parade {

    public RoueDeSecours() {
        super("Roue de Secours", "images/cartes/Secours.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParRoueDeSecours();
    }
}
