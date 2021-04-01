package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class LimiteVitesse extends Attaque {
    public LimiteVitesse() {
        super("Limite de vitesse");
    }

    @Override
    public boolean estContreeParFinDeLimite() {
        return true;
    }

    @Override
    public boolean estContreeParVehiculePrioritaire() {
        return true;
    }
}
