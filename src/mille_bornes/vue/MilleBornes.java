package mille_bornes.vue;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mille_bornes.controleur.BarreMenu;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.vue.jeu.Sabot;
import mille_bornes.vue.joueur.HJoueurMain;
import mille_bornes.vue.joueur.JoueurMain;
import mille_bornes.vue.joueur.VJoueurMain;

import java.io.IOException;
import java.util.Arrays;

public class MilleBornes {

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private Jeu jeu;

    private JoueurMain[] mains = new JoueurMain[4];

    public MilleBornes(double width, double height) throws IOException {
        contenu = new BorderPane();
        sabot = new Sabot(null, this);

        FXMLLoader loader = new FXMLLoader(MilleBornes.class.getResource("/fxml/barre-menu.fxml"));
        MenuBar barreMenu = loader.load();
        Object controller = loader.getController();

        if (controller instanceof BarreMenu) {
            ((BarreMenu) controller).setGui(this);
        }

        vBox = new VBox();

        contenu.setBackground(new Background(new BackgroundFill(Color.rgb(158, 251, 144), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getChildren().addAll(barreMenu, contenu);

        contenu.setPrefWidth(width);
        contenu.setPrefHeight(height);
        contenu.setPadding(new Insets(5, 5, 5, 5));
        contenu.setCenter(sabot);
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        this.jeu.prepareJeu();
        sabot.setJeu(jeu);
        Arrays.fill(mains, null);
        mains[0] = new HJoueurMain(this, jeu.getJoueurs().get(0), true);

        // Selection manuelle des emplacements des joueurs selon le nombre total
        switch (jeu.getNbJoueurs()) {
            case 2:
                mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(1), false);
                break;
            case 3:
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(2), false);
                break;
            case 4:
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false);
                mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(2), false);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(3), false);
                break;
        }

        for (int i = 1; i < mains.length; i++) {
            if (mains[i] != null) mains[i].cacher();
        }

        contenu.setBottom(mains[0]);
        contenu.setLeft(mains[1]);
        contenu.setTop(mains[2]);
        contenu.setRight(mains[3]);
        jeu.activeProchainJoueurEtTireCarte();
    }

    public void tournerJoueurs() {
        Joueur joueur = jeu.getJoueurActif();

        mains[0].setJoueur(joueur);

        for (int i = 0; i < mains.length; i++) {
            if (mains[i] == null) continue;
            joueur = joueur.getProchainJoueur();
            mains[i].setJoueur(joueur);
        }
    }

    public Sabot getSabot() {
        return sabot;
    }

    public BorderPane getContenu() {
        return contenu;
    }

    public VBox getHolder() {
        return vBox;
    }

    public void carteCliquee(Carte carte, boolean clicDroit) {
        try {
            if (clicDroit) {
                jeu.getJoueurActif().defausseCarte(jeu, carte);
            } else {
                if (carte instanceof Attaque) {
                    // afficher alert
                    Joueur cible = new ChoisitDestination(jeu, (Attaque) carte).getCible();

                    jeu.getJoueurActif().joueCarte(jeu, carte, cible);
                } else {
                    jeu.getJoueurActif().joueCarte(jeu, carte);
                }
            }
            jeu.activeProchainJoueurEtTireCarte();
            tournerJoueurs();
        } catch (IllegalStateException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText("Vous ne pouvez pas faire cette action");
            error.setContentText(e.getMessage());
        }
    }
}