package mille_bornes.modele.cartes.bottes;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class Increvable extends Botte {

    public Increvable() {
        super("Increvable", "images/cartes/Increvable.jpg");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParIncrevable();
    }
}
