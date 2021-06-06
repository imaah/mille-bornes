package mille_bornes.vue;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import mille_bornes.application.Asset;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class APropos extends Alert {

    /**
     * Créer une boîte de dialogue "À propos"
     */
    public APropos() {
        // Puisqu'on hérite d'Alert
        super(AlertType.INFORMATION);
        this.setTitle("À propos");

        // On récupère la bannière du jeu
        ImageView headerImage = new ImageView(Asset.FOND_BOITE_APROPOS.getImage());
        headerImage.setPreserveRatio(true);
        headerImage.setFitWidth(512);

        HBox imageBox = new HBox(headerImage);
        imageBox.setAlignment(Pos.CENTER);

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        // On affiche le texte
        Label lbl = new Label("Jeu du Mille bornes\nPar MORISSE Esteban & GUIBOUT Clément\n" + format.format(date));
        lbl.setStyle("-fx-font-size: 18pt; -fx-text-fill: orange; -fx-font-weight: bolder; -fx-text-alignment: center;");
        HBox content = new HBox(lbl);

        this.getDialogPane().setHeader(imageBox);
        content.setAlignment(Pos.BOTTOM_CENTER);
        this.getDialogPane().setContent(content);
    }
}
