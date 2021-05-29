package mille_bornes.vue;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.controleur.BarreMenu;
import mille_bornes.vue.jeu.Sabot;
import mille_bornes.vue.joueur.HJoueurMain;
import mille_bornes.vue.joueur.JoueurMain;
import mille_bornes.vue.joueur.VJoueurMain;
import mille_bornes.modele.cartes.attaques.Accident;
import mille_bornes.modele.cartes.attaques.Crevaison;
import mille_bornes.modele.cartes.attaques.FeuRouge;
import mille_bornes.modele.cartes.bottes.AsDuVolant;
import mille_bornes.modele.cartes.bottes.Citerne;
import mille_bornes.modele.cartes.bottes.Increvable;
import mille_bornes.modele.cartes.bottes.VehiculePrioritaire;
import mille_bornes.modele.cartes.parades.FeuVert;

import java.io.IOException;

public class MilleBornes {

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private Jeu jeu;

    public MilleBornes(double width, double height) throws IOException {
        contenu = new BorderPane();
        Joueur bob = new Joueur("bob");
        Joueur bob2 = new Joueur("bob2");
        Joueur bob3 = new Joueur("bob3");
        Joueur bob4 = new Joueur("bob4");

        jeu = new Jeu(bob, bob2, bob3, bob4);
        jeu.prepareJeu();
        sabot = new Sabot(jeu);
        FXMLLoader loader = new FXMLLoader(MilleBornes.class.getResource("/fxml/barre-menu.fxml"));
        Object controller = loader.getController();

        if (controller instanceof BarreMenu) {
            ((BarreMenu) controller).setGui(this);
        }

        vBox = new VBox();

        contenu.setBackground(new Background(new BackgroundFill(Color.rgb(158, 251, 144), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getChildren().addAll(loader.load(), contenu);

        contenu.setPrefWidth(width);
        contenu.setPrefHeight(height);
        contenu.setPadding(new Insets(5, 5, 5, 5));
        contenu.setCenter(sabot);

        JoueurMain main1 = new HJoueurMain(bob, true);
        JoueurMain main2 = new HJoueurMain(bob2, false, true);
        JoueurMain main3 = new VJoueurMain(bob3, false);
        JoueurMain main4 = new VJoueurMain(bob4, false, true);

        bob.getEtat().setLimiteVitesse(true);
        bob2.getEtat().setLimiteVitesse(true);
        bob.prendCarte(new Citerne());

//        bob.getEtat().addBotte(new VehiculePrioritaire());
        bob.getEtat().addBotte(new Citerne());
//        bob.getEtat().addBotte(new Increvable());
        bob.getEtat().addBotte(new AsDuVolant());

//        bob2.getEtat().addBotte(new VehiculePrioritaire());
//        bob2.getEtat().addBotte(new Citerne());
//        bob2.getEtat().addBotte(new Increvable());
//        bob2.getEtat().addBotte(new AsDuVolant());

        bob3.getEtat().addBotte(new VehiculePrioritaire());
//        bob3.getEtat().addBotte(new Citerne());
//        bob3.getEtat().addBotte(new Increvable());
//        bob3.getEtat().addBotte(new AsDuVolant());

//        bob4.getEtat().addBotte(new VehiculePrioritaire());
//        bob4.getEtat().addBotte(new Citerne());
        bob4.getEtat().addBotte(new Increvable());
//        bob4.getEtat().addBotte(new AsDuVolant());

        bob.getEtat().setBataille(new FeuVert());
        bob2.getEtat().setBataille(new Accident());
        bob3.getEtat().setBataille(new Crevaison());
        bob4.getEtat().setBataille(new FeuRouge());

        main1.update();
        main2.update();
        main3.update();
        main4.update();

        main2.cacher();
        main3.cacher();
        main4.cacher();

        contenu.setBottom(main1.getHolder());
        contenu.setTop(main2.getHolder());
        contenu.setLeft(main3.getHolder());
        contenu.setRight(main4.getHolder());
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        // TODO: 24/05/2021 Mettre à jour les différentes vues.
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
}
