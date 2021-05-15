package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class Increvable extends Botte {
    private static final long serialVersionUID = 2000507117556205771L;

    public Increvable() {
        super("Increvable");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParIncrevable();
    }
}
