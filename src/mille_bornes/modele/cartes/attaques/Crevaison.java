package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class Crevaison extends Attaque {
    public Crevaison() {
        super("Crevaison", "images/cartes/Creve.jpg");
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
