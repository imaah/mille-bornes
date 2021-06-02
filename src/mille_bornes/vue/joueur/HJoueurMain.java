package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import mille_bornes.modele.Joueur;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

public class HJoueurMain extends JoueurMain {

    public HJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this(milleBornes, joueur, survolActif, false);
    }

    public HJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif, boolean invert) {
        super(milleBornes, joueur, survolActif);

        setAlignment(Pos.CENTER);

        add(limite, 1, 2);
        add(bataille, 0, 2);

        if (invert) {
            addRow(1, cartes);
            addRow(3, statusLabel);
            for(CarteVue carte : getBottes()) {
                carte.tourner(CarteVue.CarteRotation.DEG_180);
            }
            limite.tourner(CarteVue.CarteRotation.DEG_180);
            bataille.tourner(CarteVue.CarteRotation.DEG_180);
        } else {
            addRow(3, cartes);
            addRow(1, statusLabel);
        }

        for (int i = 0; i < bottes.length; i++) {
            add(bottes[i], 2 + i, 2);
        }
    }
}
