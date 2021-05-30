package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class Crevaison extends Attaque {
    private static final long serialVersionUID = -6364972573271083903L;

    public Crevaison() {
        super("Crevaison", "assets/cartes/Creve.jpg");
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
