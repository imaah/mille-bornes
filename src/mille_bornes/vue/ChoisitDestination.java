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
import mille_bornes.modele.cartes.Botte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.vue.exceptions.PasDeCiblePossibleException;

import java.util.*;

/**
 * Permet d'afficher une boîte de dialogue avec la liste des autres joueurs pour attaquer un joueur
 */
public class ChoisitDestination extends Alert {

    /**
     * Ouvre une boite de dialogue avec la liste des joueurs de la partie. Permet de lancer une attaque sur un d'entre
     * eux. Si un joueur est déjà attaqué, une erreur sera levée avec un nouvelle boîte de dialogue. Sinon, il recoit
     * la carte sur sa pile de bataille.
     *
     * @param jeu   La partie actuelle
     * @param carte La carte attaquante
     */
    private ChoisitDestination(Jeu jeu, Attaque carte) {
        // Personnalisation de la boite de dialogue
        super(AlertType.CONFIRMATION);
        this.setTitle("Choix d'un adversaire");
        this.setHeaderText("Vers qui envoyer " + carte.toString());
    }

    public static Joueur getCible(Jeu jeu, Attaque carte) throws PasDeCiblePossibleException {
        if(jeu.getNbJoueurs() == 1) {
            Joueur cible = jeu.getJoueurActif().getProchainJoueur();
            if((cible.getBataille() instanceof Attaque && !(carte instanceof LimiteVitesse))
                    || (carte instanceof LimiteVitesse && cible.getLimiteVitesse())) {
                throw new PasDeCiblePossibleException();
            }
            return cible;
        }
        Alert alert = new ChoisitDestination(jeu, carte);

        Map<String, Joueur> joueurMap = new HashMap<>();

        // Création des composants d'affichage
        List<RadioButton> boutons = new ArrayList<>();
        FlowPane affichage = new FlowPane();
        ToggleGroup groupe = new ToggleGroup();
        affichage.setAlignment(Pos.BASELINE_LEFT);

        // Chaque joueur de la partie obtiens un bouton
        for (Joueur joueur : jeu.getJoueurs()) {
            if (joueur.equals(jeu.getJoueurActif())) continue;

            if((joueur.getBataille() instanceof Attaque && !(carte instanceof LimiteVitesse))
                    || (carte instanceof LimiteVitesse && joueur.getLimiteVitesse())) continue;

            boolean botteContre = false;
            for(Botte botte : joueur.getBottes()) {
                if(botte.contre(carte)) {
                    botteContre = true;
                    break;
                }
            }
            if(botteContre) continue;

            FlowPane flowPane = new FlowPane();
            flowPane.setPadding(new Insets(5));

            RadioButton btn = new RadioButton(joueur.nom);
            btn.setToggleGroup(groupe);
            joueurMap.put(joueur.nom, joueur);
            boutons.add(btn);

            flowPane.getChildren().add(btn);
            affichage.getChildren().add(flowPane);
        }

        alert.getDialogPane().setContent(affichage);

        if(boutons.size() == 0) {
            throw new PasDeCiblePossibleException();
        } else if(boutons.size() == 1) {
            return joueurMap.get(boutons.get(0).getText());
        }

        // On récupère la réponse
        Optional<ButtonType> confirmation = alert.showAndWait();

        Joueur tempCible = null;

        // Si c'est OK
        if (confirmation.orElse(null) == ButtonType.OK) {
            for (RadioButton btn : boutons) {
                if (btn.isSelected()) {
                    tempCible = joueurMap.get(btn.getText());
                    break;
                }
            }
        }

        return tempCible;
    }
}
