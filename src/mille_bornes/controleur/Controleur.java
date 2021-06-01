package mille_bornes.controleur;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.extensions.bots.DumbBot;
import mille_bornes.modele.extensions.bots.NaiveBot;
import mille_bornes.modele.utils.JsonUtils;
import mille_bornes.vue.MilleBornes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controleur {
    private final MilleBornes milleBornes;

    public Controleur(MilleBornes milleBornes) {
        this.milleBornes = milleBornes;
    }

    public void joueCarte(Carte carte) {
        milleBornes.joueCarte(carte);
    }

    public void defausseCarte(Carte carte) {
        milleBornes.defausseCarte(carte);
    }

    /**
     * Demande confirmation avant de quitter l'application ou de changer de partie
     */
    public boolean confirmation(String title, String header, String content) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(title);
        confirmation.setHeaderText(header);
        confirmation.setContentText(content);

        Optional<ButtonType> reponse = confirmation.showAndWait();

        if (reponse.orElse(null) == ButtonType.CANCEL) {
            return false;
        } else {
            System.out.println("Fermeture du MilleBornes.");
            return true;
        }
    }

    public void nouvellePartie() {
        // On vérifie d'abord qu'aucune partie n'est en cours
        if (!(milleBornes.getJeu() == null || milleBornes.getJeu().estPartieFinie())) {
            // Si il ne souhaite pas recréer de partie, alors on annule
            if (!confirmation(
                    "Nouvelle partie",
                    "Une partie est déjà en cours, êtes-vous sûr de vouloir créer une nouvelle partie ?",
                    "Si vous créez une nouvelle partie, l'état de celle en cours ne sera pas sauvegardé!"
            )) return;
        }

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

                FlowPane ligne = new FlowPane();
                ligne.setAlignment(Pos.CENTER);
                ligne.setPadding(new Insets(5));
                ligne.setHgap(5);
                ligne.getChildren().addAll(joueur.getLabel(), joueur.getComboBox(), joueur.getTextField());

                affichage.getChildren().add(ligne);
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
                    String tempNom;
                    if ((tempNom = j.getTextField().getText().trim()).equals("")) {
                        tempNom = "Joueur n°" + (total.indexOf(j) + 1);
                    }
                    String nom = tempNom;

                    // On évite les doublons
                    int n = 1;
                    tempNom = nom;
                    while (joueurs.stream().map(Joueur::getNom).collect(Collectors.toList()).contains(tempNom)) {
                        tempNom = nom + " " + (++n);
                    }

                    nom = tempNom;

                    if ("IA - Aléatoire".equals(value)) {
                        joueurs.add(new DumbBot(nom));
                    } else if ("IA - Naïf".equals(value)) {
                        joueurs.add(new NaiveBot(nom));
                    } else {
                        joueurs.add(new Joueur(nom));
                    }
                }

                Jeu partie = new Jeu(joueurs.toArray(new Joueur[0]));
                milleBornes.setJeu(partie);
            }
        }
    } // nouvellePartie

    public void sauvegarder() {
        FileChooser enregistreur = new FileChooser();
        enregistreur.setTitle("Enregistrer une partie");
        enregistreur.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers de sauvegarde", "*.json"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File fichier = enregistreur.showSaveDialog(null);

        // Si on a un chemin, alors on essaye d'écrire dedans au format JSON
        if (fichier != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = milleBornes.getJeu().sauvegarder();
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
    } // sauvegarder

    public void quitterClique() {
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

    public void chargerPartie() {
        FileChooser ouvreur = new FileChooser();
        ouvreur.setTitle("Récupérer une partie...");
        ouvreur.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers de sauvegarde", "*.json"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File fichier = ouvreur.showOpenDialog(null);

        // Si on a un fichier, alors on créé un nouveau jeu
        if (fichier != null) {
            try {
                Jeu partie = JsonUtils.chargerJeuDepuisFichier(fichier);
                milleBornes.setJeu(partie, true);

                System.out.println("Partie chargée");
            } catch (Exception e) {
                // Si on a pas pu reprendre le jeu, alors on affiche une erreur
                System.out.println(e.getMessage());

                Alert erreur = new Alert(Alert.AlertType.ERROR);
                erreur.setTitle("Ouverture d'une partie");
                erreur.setHeaderText(null);
                erreur.setContentText(String.format("Impossible d'ouvrir la partie %s !", fichier.getAbsolutePath()));
                erreur.setResizable(false);
                erreur.showAndWait();
            }
        }
    }

    public void demanderRejouer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Voulez vous rejouer ?");
        alert.setTitle("Rejouer ?");
        Optional<ButtonType> res = alert.showAndWait();

        if(res.orElse(ButtonType.NO) == ButtonType.YES) {
            nouvellePartie();
        } else {
            quitterClique();
        }
    }

    /**
     * Permet de lister les joueurs pour l'ajout. On peut ensuite récupérer les valeurs entrées
     */
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
