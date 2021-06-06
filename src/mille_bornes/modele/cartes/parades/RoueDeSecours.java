package mille_bornes.modele.cartes.parades;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class RoueDeSecours extends Parade {

    public RoueDeSecours() {
        super("Roue de Secours", Asset.CARTE_ROUE_DE_SECOURS);
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParRoueDeSecours();
    }
}
