package mille_bornes.controleur;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.vue.APropos;
import mille_bornes.vue.MilleBornes;
import mille_bornes.modele.extensions.bots.DumbBot;
import mille_bornes.modele.extensions.bots.NaiveBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        // Panneau qui va contenir la liste et le label
        FlowPane choixJoueurs = new FlowPane();
        choixJoueurs.setPadding(new Insets(5));
        choixJoueurs.setHgap(5);

        // La liste
        ComboBox<Integer> nbJoueurs = new ComboBox<>();
        nbJoueurs.getItems().addAll(2, 3, 4);
        nbJoueurs.getSelectionModel().select(0);

        // Le label
        choixJoueurs.getChildren().add(new Label("Combien de joueurs ?"));
        choixJoueurs.getChildren().add(nbJoueurs);

        alert.getDialogPane().setContent(choixJoueurs);


        // On récupère la réponse
        Optional<ButtonType> reponse = alert.showAndWait();

        if (reponse.orElse(null) == ButtonType.OK) {
            // Nouvelle boite de dialogue
            Alert choixTypesJoueurs = new Alert(Alert.AlertType.CONFIRMATION);
            choixTypesJoueurs.setTitle("Nouveaux joueurs");
            choixTypesJoueurs.setHeaderText("Indiquez le type et le nom de chaque joueur");

            // Une ArrayList de FlowPane total, qui va contenir les differentes lignes pour y accéder plus tard
            List<ChoixJoueur> total = new ArrayList<>();


            FlowPane affichage = new FlowPane();
            // Pour chaque joueur
            for (int i = 0; i < nbJoueurs.getValue(); i++) {
                ChoixJoueur joueur = new ChoixJoueur(i);
                total.add(joueur);

                FlowPane l = new FlowPane();
                l.setAlignment(Pos.CENTER);
                l.setPadding(new Insets(5));
                l.setHgap(5);
                l.getChildren().addAll(joueur.getLabel(), joueur.getComboBox(), joueur.getTextField());

                affichage.getChildren().add(l);
            }

            // Affichage
            choixTypesJoueurs.getDialogPane().setContent(affichage);

            // On récupère la réponse
            Optional<ButtonType> confirmation = choixTypesJoueurs.showAndWait();

            if (confirmation.orElse(null) == ButtonType.OK) {
                List<Joueur> joueurs = new ArrayList<>();

                for (ChoixJoueur j : total) {
                    Object value = j.getComboBox().getValue();
                    // On évite les noms vides
                    String nom;
                    if ((nom = j.getTextField().getText()).equals("")) {
                        nom = "Joueur n°" + (total.indexOf(j) + 1);
                    }

                    if ("IA - Aléatoire".equals(value)) {
                        joueurs.add(new DumbBot(nom));
                    } else if ("IA - Naïf".equals(value)) {
                        joueurs.add(new NaiveBot(nom));
                    } else {
                        joueurs.add(new Joueur(nom));
                    }

                    System.out.println(nom);
                }

                // return joueurs;
                // Ou faire quelque chose avec la liste des joueurs et des bots
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
                erreur.setContentText(String.format("Impossible de sauvegarder la partie %s !",
                        fichier.getAbsolutePath()));
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
        confirmation.setContentText("Vous êtes sur le point de quitter l'application. Si vous confirmez, " +
                                    "la partie en cours ne sera pas sauvegardée!");

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

    private static class ChoixJoueur extends FlowPane {
        final ComboBox<String> type = new ComboBox<>();
        final TextField nom = new TextField();
        final Label num = new Label();

        public ChoixJoueur(int num) {
            this.num.setText("Joueur n°" + (num + 1));

            this.type.getItems().addAll("Humain", "IA - Aléatoire", "IA - Naïf");
            this.type.getSelectionModel().select(0);

            this.nom.setPromptText("Nom du joueur");
        }

        public ComboBox<String> getComboBox() {
            return this.type;
        }

        public TextField getTextField() {
            return this.nom;
        }

        public Label getLabel() {
            return this.num;
        }
    }
}

