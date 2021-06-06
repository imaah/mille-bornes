package mille_bornes.modele.cartes.bottes;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class Citerne extends Botte {
    public Citerne() {
        super("Citerne", Asset.CARTE_CITERNE);
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParCiterne();
    }
}
