package mille_bornes.modele.cartes.attaques;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;

public class Crevaison extends Attaque {
    public Crevaison() {
        super("Crevaison", Asset.CARTE_CREVAISON);
    }

    @Override
    public boolean estContreeParRoueDeSecours() {
        return true;
    }

    @Override
    public boolean estContreeParIncrevable() {
        return true;
    }
}
