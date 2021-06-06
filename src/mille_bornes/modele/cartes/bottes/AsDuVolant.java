package mille_bornes.modele.cartes.bottes;

import mille_bornes.application.Asset;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Botte;

public class AsDuVolant extends Botte {
    public AsDuVolant() {
        super("As du volant", Asset.CARTE_AS_DU_VOLANT);
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.estContreeParAsDuVolant();
    }
}
