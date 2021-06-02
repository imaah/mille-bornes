package mille_bornes.vue.joueur;

import mille_bornes.modele.Joueur;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

public class VJoueurMain extends JoueurMain {
    public VJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this(milleBornes, joueur, survolActif, false);
    }

    public VJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif, boolean invert) {
        super(milleBornes, joueur, survolActif);

        for (CarteVue carte : cartes) {
            carte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        for (CarteVue botte : bottes) {
            botte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        limite.tourner(CarteVue.CarteRotation.DEG_90);
        bataille.tourner(CarteVue.CarteRotation.DEG_90);

        add(limite, 2, 1);
        add(bataille, 2, 0);

        if (invert) {
            addColumn(3, cartes);
            addColumn(1, statusLabel);
            for (CarteVue carte : getBottes()) {
                carte.tourner(CarteVue.CarteRotation.DEG_270);
            }
            limite.tourner(CarteVue.CarteRotation.DEG_270);
            bataille.tourner(CarteVue.CarteRotation.DEG_270);
        } else {
            addColumn(1, cartes);
            addColumn(3, statusLabel);
        }

        for (int i = 0; i < bottes.length; i++) {
            add(bottes[i], 2, 2 + i);
        }

    }
}
