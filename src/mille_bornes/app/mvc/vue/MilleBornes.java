package mille_bornes.app.mvc.vue;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mille_bornes.Jeu;
import mille_bornes.Joueur;
import mille_bornes.app.mvc.controleur.BarreMenu;

import java.io.IOException;

public class MilleBornes {

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private Jeu jeu;

    public MilleBornes() throws IOException {
        contenu = new BorderPane();
        jeu = new Jeu(new Joueur("bob"), new Joueur("bob2"));
        jeu.prepareJeu();
        sabot = new Sabot(jeu);
        FXMLLoader loader = new FXMLLoader(MilleBornes.class.getResource("barre-menu.fxml"));
        Object controller = loader.getController();

        if (controller instanceof BarreMenu) {
            ((BarreMenu) controller).setGui(this);
        }

        vBox = new VBox();

        contenu.setBackground(new Background(new BackgroundFill(Color.rgb(158, 251, 144), CornerRadii.EMPTY, Insets.EMPTY)));

        vBox.getChildren().addAll(loader.load(), contenu);
        contenu.setCenter(sabot);
//        contenu.setTop(new Sabot(jeu));
//        contenu.setBottom(new Sabot(jeu));
//        contenu.setRight(new Sabot(jeu));
//        contenu.setLeft(new Sabot(jeu));
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
