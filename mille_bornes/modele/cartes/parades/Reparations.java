package mille_bornes.modele.cartes.parades;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Parade;

public class Reparations extends Parade {
    private static final long serialVersionUID = 4135166053338020523L;

    public Reparations() {
        super("Reparations", "assets/cartes/Reparation.jpg");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParReparations();
    }
}
