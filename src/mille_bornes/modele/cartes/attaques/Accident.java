package mille_bornes.modele.cartes.attaques;

import mille_bornes.modele.cartes.Attaque;

public class Accident extends Attaque {
    public Accident() {
        super("Accident", "images/cartes/Accident.jpg");
    }

    @Override
    public boolean estContreeParReparations() {
        return true;
    }

    @Override
    public boolean estContreeParAsDuVolant() {
        return true;
    }
}
