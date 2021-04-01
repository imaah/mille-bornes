package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class FinDeLimite extends Parade {

    public FinDeLimite() {
        super("Fin de limite");
    }

    @Override
    public boolean contre(Attaque attaque) {
        return attaque.estContreeParFinDeLimite();
    }
}