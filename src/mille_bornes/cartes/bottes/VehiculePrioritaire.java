package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class VehiculePrioritaire extends Botte {
    public VehiculePrioritaire() {
        super("Vehicule Prioritaire");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParVehiculePrioritaire();
    }
}
