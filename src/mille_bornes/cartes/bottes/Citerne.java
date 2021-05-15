package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class Citerne extends Botte {
    private static final long serialVersionUID = -4792013238411995539L;

    public Citerne() {
        super("Citerne");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParCiterne();
    }
}
