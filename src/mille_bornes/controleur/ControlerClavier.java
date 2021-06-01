package mille_bornes.controleur;

import javafx.scene.input.KeyEvent;
import mille_bornes.vue.MilleBornes;

public class ControlerClavier {

    private final Controleur controleur;

    public ControlerClavier(MilleBornes milleBornes) {
        this.controleur = new Controleur(milleBornes);
        milleBornes.getHolder().setOnKeyPressed(this::touchePressee);
    }

    private void touchePressee(KeyEvent keyEvent) {
        if(keyEvent.isControlDown()) {
            char c = keyEvent.getCharacter().toLowerCase().charAt(0);

            switch (c) {
                case 'n':
                    controleur.nouvellePartie();
                    break;
                case 's':
                    controleur.sauvegarder();
                    break;
                case 'o':
                    controleur.chargerPartie();
                    break;
            }
        }
    }
}
