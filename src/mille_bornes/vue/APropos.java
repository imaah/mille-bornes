package mille_bornes.vue;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APropos extends Alert {

    public APropos() {
        super(AlertType.INFORMATION);

        this.setTitle("À propos");

        ImageView headerImage = new ImageView(getClass().getResource("/images/boite.jpg").toString());
        headerImage.setPreserveRatio(true);
        headerImage.setFitWidth(512);

        HBox imageBox = new HBox(headerImage);
        imageBox.setAlignment(Pos.CENTER);

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Label lbl = new Label("Jeu du Mille bornes\nPar MORISSE Esteban & GUIBOUT Clément\n" + format.format(date));
        lbl.setStyle("-fx-font-size: 18pt; -fx-text-fill: orange; -fx-font-weight: bolder; -fx-text-alignment: center;");
        HBox content = new HBox(lbl);

        this.getDialogPane().setHeader(imageBox);
        content.setAlignment(Pos.BOTTOM_CENTER);
        this.getDialogPane().setContent(content);
    }
}
