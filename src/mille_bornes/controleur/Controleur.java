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
import mille_bornes.modele.extensions.bots.DumbBot;
import mille_bornes.modele.extensions.bots.NaiveBot;
import mille_bornes.modele.utils.JsonUtils;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contient les methodes permettant des lier l'interface au {@link MilleBornes}
 */
public class Controleur {
    private final MilleBornes milleBornes;

    /**
     * Permet d'instancier une nouvelle partie de mille bornes existante
     * @param milleBornes La partie à charger
     */
    public Controleur(MilleBornes milleBornes) {
        this.milleBornes = milleBornes;
    }

    /**
     * Permet de jouer une carte
     * @param carte La carte à jouer
     */
    public void joueCarte(CarteVue carte) {
        milleBornes.joueCarte(carte);
    }

    /**
     * Permet de défausser une carte
     * @param carte La carte à défausser
     */
    public void defausseCarte(CarteVue carte) {
        milleBornes.defausseCarte(carte);
    }

    /**
     * Demande confirmation avant de quitter l'application ou de changer de partie
     * @param title le titre de l'alert
     * @param header texte dans l'entête de l'alert
     * @param content contenu de l'alert
     * @return si l'utilisateur à appuyé sur ok.
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

    /**
     * Dit si une partie est déjà en cours
     *
     * @return true si une partie est déjà en cours, false sinon
     */
    public boolean dejaUnePartieEnCours() {
        return !(milleBornes.getJeu() == null || milleBornes.getJeu().estPartieFinie());
    }

    /**
     * Demande si l'utilisateur veut quitter la partie en cours
     *
     * @return true s'il veut quitter, false sinon
     */
    public boolean veuxAbandonnerPartie() {
        return confirmation(
                "Nouvelle partie",
                "Une partie est déjà en cours, êtes-vous sûr de vouloir créer une nouvelle partie ?",
                "Si vous créez une nouvelle partie, l'état de celle en cours ne sera pas sauvegardé!"
        );
    }

    /**
     * Demande dans une fenêtre le nombre de joueurs à renseigner pour créer une nouvelle partie
     *
     * @return le nombre de joueurs à demander, -1 si l'utilisateur a annulé
     */
    private int getNbJoueurs() {
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
            return nbJoueurs.getValue();
        }

        return -1;
    }

    /**
     * Affiche une boite de dialogue pour savoir le nom et le type des futurs joueurs/bots du jeu
     *
     * @param nbJoueurs Le nombre de joueurs pour lesquels demander des renseignements
     * @return Une liste de joueurs avec leur nom et leur type
     */
    private List<ChoixJoueur> demandeNomsJoueurs(int nbJoueurs) {
        // Nouvelle boite de dialogue
        Alert choixTypesJoueurs = new Alert(Alert.AlertType.CONFIRMATION);
        choixTypesJoueurs.setTitle("Nouveaux joueurs");
        choixTypesJoueurs.setHeaderText("Indiquez le type et le nom de chaque joueur");

        // Une ArrayList de FlowPane total, qui va contenir les differentes lignes pour y accéder plus tard
        List<ChoixJoueur> joueurs = new ArrayList<>();


        FlowPane affichage = new FlowPane();
        // Pour chaque joueur
        for (int i = 0; i < nbJoueurs; i++) {
            ChoixJoueur joueur = new ChoixJoueur(i);
            joueurs.add(joueur);

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
            return joueurs;
        }

        return null;
    }

    /**
     * Permet de normaliser les noms des joueurs (bots compris). Si certains ne possèdes pas de nom, ils en obtiennent 1
     *
     * @param joueurs La listes des joueurs (pour connaître leur position)
     * @param joueur  Le joueur en question
     * @return Le nom du joueur normalisé
     */
    private String normaliseNomJoueur(List<ChoixJoueur> joueurs, ChoixJoueur joueur) {
        Object value = joueur.getComboBox().getValue();
        // On évite les noms vides
        String tempNom;

        // Si c'est un humain et qu'il n'a pas de nom, on lui en met un
        if ((tempNom = joueur.getTextField().getText().trim()).equals("")) {
            if (value.equals("Humain")) {
                tempNom = "Joueur n°" + (joueurs.indexOf(joueur) + 1);
            } else {
                tempNom = "Bot n°" + (joueurs.indexOf(joueur) + 1);
            }

        }

        return tempNom;
    }

    /**
     * Permet de créer une nouvelle partie
     */
    public void nouvellePartie() {
        // On vérifie d'abord qu'aucune partie n'est en cours
        if (dejaUnePartieEnCours()) {
            if (!veuxAbandonnerPartie()) return;
        }

        int nbJoueurs = getNbJoueurs();

        // Si getNbJoueurs renvoit 0, l'utilisateur ne veut plus créer de partie
        if (nbJoueurs == -1) return;

        List<ChoixJoueur> joueurs = demandeNomsJoueurs(nbJoueurs);

        // Si demandeNomsJoueurs renvoit null, l'utilisateur ne veut plus créer de partie
        if (joueurs == null) return;


        List<Joueur> joueursFinaux = new ArrayList<>();
        for (ChoixJoueur j : joueurs) {
            // On s'assure que chaque joueur a un nom
            String nom = normaliseNomJoueur(joueurs, j);

            // On évite les doublons
            int n = 1;
            while (joueursFinaux.stream().map(Joueur::getNom).collect(Collectors.toList()).contains(nom)) {
                nom = nom + " " + (++n);
            }


            String type = j.getComboBox().getValue();
            // On créer enfin la liste de joueurs
            if (type.equals("IA - Aléatoire")) {
                joueursFinaux.add(new DumbBot(nom));
            } else if (type.equals("IA - Naïf")) {
                joueursFinaux.add(new NaiveBot(nom));
            } else {
                joueursFinaux.add(new Joueur(nom));
            }
        }

        Jeu partie = new Jeu(joueursFinaux.toArray(new Joueur[0]));
        milleBornes.setJeu(partie);
    } // nouvellePartie

    /**
     * Sauvegarde l'état de la partie en cours dans un fichier .json
     */
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

    /**
     * Demande à l'utilisateur s'il veut vraiment quitter l'application
     */
    public void quitterClique() {
        String message = "Vous êtes sur le point de fermer l'application. Êtes-vous sûr de vouloir quitter ?";
        if (dejaUnePartieEnCours()) message += " Comme une partie est déjà en cours, la fermeture de" +
                                               " l'application sans sauvegarde entrainera la perte de" +
                                               " l'état de la partie!";
        if (confirmation("Quitter",
                "Voulez-vous vraiment quitter ?",
                message
        )) System.exit(0);
    }

    /**
     * Ouvre un fichier de sauvegarde de partie, montre une boite de dialogue d'erreur s'il y a un soucis
     */
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

    /**
     * Affiche une boite de dialogue qui demande à l'utilisateur s'il veut rejouer
     */
    public void demanderRejouer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Voulez vous rejouer ?");
        alert.setTitle("Rejouer ?");
        Optional<ButtonType> res = alert.showAndWait();

        if (res.orElse(null) == ButtonType.YES) {
            nouvellePartie();
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

        /**
         * Permet de récupérer le type du joueur
         *
         * @return Le type du joueur
         */
        public ComboBox<String> getComboBox() {
            return this.type;
        }

        /**
         * Permet de récupérer le nom entré
         *
         * @return Le nom du joueur
         */
        public TextField getTextField() {
            return this.nom;
        }

        /**
         * Permet de récupérer le numéro du joueur
         *
         * @return Le numéro du joueur
         */
        public Label getLabel() {
            return this.num;
        }
    }
}
