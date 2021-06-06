package mille_bornes.modele.cartes.bottes;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class Increvable extends Botte {

    public Increvable() {
        super("Increvable", Asset.CARTE_INCREVABLE);
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParIncrevable();
    }
}
