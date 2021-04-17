package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class AsDuVolant extends Botte {
    public AsDuVolant() {
        super("As du volant");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParAsDuVolant();
    }
}
