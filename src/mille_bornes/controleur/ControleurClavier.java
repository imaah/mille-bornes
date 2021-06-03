package mille_bornes.controleur;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import mille_bornes.vue.MilleBornes;

public class ControleurClavier {

    private ControleurClavier(Scene scene, MilleBornes milleBornes) {
        Controleur controleur = new Controleur(milleBornes);
        scene.setOnKeyPressed(this::touchePressee);
    }

    public static void init(Scene scene, MilleBornes app) {
        new ControleurClavier(scene, app);
    }

    private void touchePressee(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getText().length() > 0) {
            char c = keyEvent.getText().toLowerCase().charAt(0);

            switch (c) {
                case 'n':
//                    controleur.nouvellePartie();
                    break;
                case 's':
//                    controleur.sauvegarder();
                    break;
                case 'o':
//                    controleur.chargerPartie();
                    break;
            }
        }
    }
}
