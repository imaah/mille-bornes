package mille_bornes.cartes.bottes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;

public class AsDuVolant extends Botte {
    private static final long serialVersionUID = 4243382288072889349L;

    public AsDuVolant() {
        super("As du volant", "assets/cartes/As_Volant.jpg");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParAsDuVolant();
    }
}
