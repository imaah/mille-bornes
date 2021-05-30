package mille_bornes.modele.cartes.bottes;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class Increvable extends Botte {
    private static final long serialVersionUID = 2000507117556205771L;

    public Increvable() {
        super("Increvable", "assets/cartes/Increvable.jpg");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParIncrevable();
    }
}
