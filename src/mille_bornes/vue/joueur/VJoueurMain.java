package mille_bornes.vue.joueur;

import mille_bornes.modele.Joueur;
import mille_bornes.vue.jeu.CarteVue;

public class VJoueurMain extends JoueurMain {
    public VJoueurMain(Joueur joueur, boolean survolActif) {
        this(joueur, survolActif, false);
    }

    public VJoueurMain(Joueur joueur, boolean survolActif, boolean invert) {
        super(joueur, survolActif);

        for (CarteVue carte : cartes) {
            carte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        for (CarteVue botte : bottes) {
            botte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        limite.tourner(CarteVue.CarteRotation.DEG_90);
        bataille.tourner(CarteVue.CarteRotation.DEG_90);

        pane.add(limite, 2, 1);
        pane.add(bataille, 2, 0);

        if (invert) {
            pane.addColumn(3, cartes);
            pane.addColumn(1, statusLabel);
        } else {
            pane.addColumn(1, cartes);
            pane.addColumn(3, statusLabel);
        }

        for (int i = 0; i < bottes.length; i++) {
            pane.add(bottes[i], 2, 2 + i);
        }

    }
}
