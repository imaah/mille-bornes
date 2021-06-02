package mille_bornes.vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Attaque;

import java.util.*;

public class ChoisitDestination extends Alert {
    /** Le joueur cible qui va recevoir l'attaque */
    private final Joueur cible;


    /**
     * Ouvre une boite de dialogue avec la liste des joueurs de la partie. Permet de lancer une attaque sur un d'entre
     * eux. Si un joueur est déjà attaqué, une erreur sera levée avec un nouvelle boîte de dialogue. Sinon, il recoit
     * la carte sur sa pile de bataille.
     * @param jeu La partie actuelle
     * @param carte La carte attaquante
     */
    public ChoisitDestination(Jeu jeu, Attaque carte) {
        // Personnalisation de la boite de dialogue
        super(AlertType.CONFIRMATION);
        this.setTitle("Choix d'un adversaire");
        this.setHeaderText("Vers qui envoyer " + carte.toString());
        // Mapping des noms et des joueurs, pour plus tard retrouver le nom du joueur associé au bouton
        Map<String, Joueur> joueurMap = new HashMap<>();

        // Création des composants d'affichage
        List<RadioButton> boutons = new ArrayList<>();
        FlowPane affichage = new FlowPane();
        ToggleGroup groupe = new ToggleGroup();
        affichage.setAlignment(Pos.BASELINE_LEFT);

        // Chaque joueur de la partie obtiens un bouton
        for (Joueur joueur : jeu.getJoueurs()) {
            if (joueur.equals(jeu.getJoueurActif())) continue;

            FlowPane l = new FlowPane();
            l.setPadding(new Insets(5));

            RadioButton btn = new RadioButton(joueur.nom);
            btn.setToggleGroup(groupe);
            joueurMap.put(joueur.nom, joueur);
            boutons.add(btn);

            l.getChildren().add(btn);
            affichage.getChildren().add(l);
        }

        this.getDialogPane().setContent(affichage);

        Optional<ButtonType> confirmation = this.showAndWait();

        Joueur tempCible = null;

        if (confirmation.orElse(null) == ButtonType.OK) {
            for (RadioButton btn : boutons) {
                if (btn.isSelected()) {
                    tempCible = joueurMap.get(btn.getText());
                    break;
                }
            }
        }

        this.cible = tempCible;
    }

    /**
     * Permet de récupérer la cible choisie
     * @return La cible choisie, ou null si aucune
     */
    public Joueur getCible() {
        return cible;
    }
}
