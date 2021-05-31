package mille_bornes.modele.cartes.bottes;

import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class AsDuVolant extends Botte {
    public AsDuVolant() {
        super("As du volant", "images/cartes/As_Volant.jpg");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParAsDuVolant();
    }
}
