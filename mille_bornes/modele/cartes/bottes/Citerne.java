package mille_bornes.modele.cartes.bottes;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class Citerne extends Botte {
    private static final long serialVersionUID = -4792013238411995539L;

    public Citerne() {
        super("Citerne", "assets/cartes/Citerne.jpg");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParCiterne();
    }
}
