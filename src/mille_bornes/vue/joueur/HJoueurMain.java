package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import mille_bornes.modele.Joueur;

public class HJoueurMain extends JoueurMain {

    public HJoueurMain(Joueur joueur, boolean survolActif) {
        this(joueur, survolActif, false);
    }

    public HJoueurMain(Joueur joueur, boolean survolActif, boolean invert) {
        super(joueur, survolActif);

        pane.setAlignment(Pos.CENTER);

        pane.add(limite, 1, 2);
        pane.add(bataille, 0, 2);

        if (invert) {
            pane.addRow(1, cartes);
            pane.addRow(3, statusLabel);
        } else {
            pane.addRow(3, cartes);
            pane.addRow(1, statusLabel);
        }

        for (int i = 0; i < bottes.length; i++) {
            pane.add(bottes[i], 2 + i, 2);
        }
    }
}
