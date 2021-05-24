package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class Accident extends Attaque {

    private static final long serialVersionUID = 5238001142998587769L;

    public Accident() {
        super("Accident", "assets/cartes/Accident.jpg");
    }

    @Override
    public boolean estContreeParAsDuVolant() {
        return true;
    }

    @Override
    public boolean estContreeParReparations() {
        return true;
    }
}
