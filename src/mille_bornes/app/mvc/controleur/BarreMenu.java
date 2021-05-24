package mille_bornes.app.mvc.controleur;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mille_bornes.Jeu;
import mille_bornes.app.mvc.vue.APropos;
import mille_bornes.app.mvc.vue.MilleBornes;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 *
 */
public class BarreMenu {
    private Jeu partie;
    private MilleBornes gui;

    public BarreMenu() {
    }

    /**
     * Permet de créer une nouvelle partie avec des options
     */
    public void onCreerNouvellePartie() {
        // Choix du nombre de joueurs
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choix du nombre de joueurs");
        alert.setHeaderText("Combien de joueurs ?");

        ComboBox<Integer> nbJoueurs = new ComboBox<>();
        nbJoueurs.getItems().addAll(1, 2, 3, 4);

        FlowPane choixJoueurs = new FlowPane();
        choixJoueurs.setPadding(new Insets(5));
        choixJoueurs.setHgap(5);

        choixJoueurs.getChildren().add(new Label("Combien de joueurs ?"));
        choixJoueurs.getChildren().add(nbJoueurs);

        alert.getDialogPane().setContent(choixJoueurs);


        // On récupère la réponse
        Optional<ButtonType> reponse = alert.showAndWait();

        if (reponse.orElse(null) == ButtonType.OK) {
            Alert choixTypesJoueurs = new Alert(Alert.AlertType.CONFIRMATION);
            choixTypesJoueurs.setTitle("Nouveaux joueurs");
            choixTypesJoueurs.setHeaderText("Indiquez le type et le nom de chaque joueur");

            FlowPane joueurs = new FlowPane();
            joueurs.setPadding(new Insets(5));
            joueurs.setHgap(5);

            for (int i = 1; i <= nbJoueurs.getValue(); i++) {
                // Ajout des labels
                joueurs.getChildren().add(new Label("Joueur n°" + i));

                // Ajout des listes
                ComboBox<String> type = new ComboBox<>();
                type.getItems().addAll("IA", "Humain");
                joueurs.getChildren().add(type);

                // Ajout du choix des noms
            }
        }
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public void setGui(MilleBornes gui) {
        this.gui = gui;
    }

    /**
     * Ouvre une partie et créé un nouveau jeu à partir du contenu du fichier lu
     */
    public void onChargerPartie() {
        FileChooser ouvreur = new FileChooser();
        ouvreur.setTitle("Récupérer une partie...");
        ouvreur.getExtensionFilters().addAll(
                new ExtensionFilter("Fichiers de sauvegarde", "*.json"),
                new ExtensionFilter("Tous les fichiers", "*.*")
        );
        File fichier = ouvreur.showOpenDialog(null);

        // Si on a un fichier, alors on créé un nouveau jeu
        if (fichier != null) {
            try {
                Gson gson = new Gson();
                // TODO: Faire ça proprement

                this.partie = new Jeu((JsonObject) gson.fromJson(String.valueOf(fichier), Object.class));
            } catch (Exception e) {
                // Si on a pas pu reprendre le jeu, alors on affiche une erreur
                Alert erreur = new Alert(Alert.AlertType.ERROR);
                erreur.setTitle("Ouverture d'une partie");
                erreur.setHeaderText(null);
                erreur.setContentText(String.format("Impossible d'ouvrir la partie %s !", fichier.getAbsolutePath()));
                erreur.setResizable(false);
                erreur.showAndWait();
            }

        }
    }

    /**
     * Permet de sauvegarder l'état d'une partie au format JSON
     */
    public void onSauverPartie() {
        FileChooser enregistreur = new FileChooser();
        enregistreur.setTitle("Enregistrer une partie");
        enregistreur.getExtensionFilters().addAll(
                new ExtensionFilter("Fichiers de sauvegarde", "*.json"),
                new ExtensionFilter("Tous les fichiers", "*.*")
        );
        File fichier = enregistreur.showSaveDialog(null);

        // Si on a un chemin, alors on essaye d'écrire dedans au format JSON
        if (fichier != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = partie.sauvegarder();
            FileWriter writer;
            try {
                writer = new FileWriter(fichier);
                gson.toJson(json, writer);

                writer.flush();
                writer.close();
            } catch (IOException e) {
                // Si on a pas pu écrire, alors on affiche une erreur
                Alert erreur = new Alert(Alert.AlertType.ERROR);
                erreur.setTitle("Sauvegarde d'une partie");
                erreur.setHeaderText(null);
                erreur.setContentText(String.format("Impossible de sauvegarder la partie %s !", fichier.getAbsolutePath()));
                erreur.setResizable(false);
                erreur.showAndWait();
            }
        }
    }

    /**
     * Demande confirmation avant de quitter l'application
     */
    public void onQuitter() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Quitter");
        confirmation.setHeaderText("Voulez-vous vraiment quitter ?");
        confirmation.setContentText("Vous êtes sur le point de quitter l'application. Si vous confirmez, la partie en cours ne sera pas sauvegardée!");

        Optional<ButtonType> reponse = confirmation.showAndWait();

        if (reponse.orElse(null) == ButtonType.OK) {
            System.out.println("Fermeture du MilleBornes.");
            System.exit(0);
        }
    }

    /**
     * Créé un nouvel objet APropos pour afficher la fenêtre
     */
    public void onAPropos() {
        Alert alert = new APropos();
        alert.showAndWait();
    }
}
