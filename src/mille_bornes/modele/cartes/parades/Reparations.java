package mille_bornes.modele.cartes.parades;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class Reparations extends Parade {

    public Reparations() {
        super("Reparations", Asset.CARTE_REPARATION);
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParReparations();
    }
}
