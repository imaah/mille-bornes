package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class Citerne extends Botte {
    public Citerne() {
        super("Citerne");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParCiterne();
    }
}
