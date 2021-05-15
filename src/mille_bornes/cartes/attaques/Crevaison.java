package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class Crevaison extends Attaque {
    private static final long serialVersionUID = -6364972573271083903L;

    public Crevaison() {
        super("Crevaison");
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
