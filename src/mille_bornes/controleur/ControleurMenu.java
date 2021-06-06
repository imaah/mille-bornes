package mille_bornes.controleur;

import javafx.scene.control.Alert;
import mille_bornes.vue.APropos;
import mille_bornes.vue.MilleBornes;

/**
 * Controleur de la barre de menu du haut.
 */
public class ControleurMenu {
    private Controleur controleur;

    /**
     * Permet de créer une nouvelle partie avec des options
     */
    public void onCreerNouvellePartie() {
        controleur.nouvellePartie();
    }


    /**
     * Permet de charger sur l'interface avec la partie actuelle
     *
     * @param gui L'instance de milleBornes à afficher
     */
    public void setGui(MilleBornes gui) {
        controleur = new Controleur(gui);
    }


    /**
     * Ouvre une partie et créé un nouveau jeu à partir du contenu du fichier lu
     */
    public void onChargerPartie() {
        controleur.chargerPartie();
    }


    /**
     * Permet de sauvegarder l'état d'une partie au format JSON
     */
    public void onSauverPartie() {
        controleur.sauvegarder();
    }


    /**
     * Appelle la même fonction onQuitter, mais avec des paramètres par défaut
     */
    public void onQuitter() {
        controleur.quitterClique();
    }

    /**
     * Créé un nouvel objet APropos pour afficher la fenêtre
     */
    public void onAPropos() {
        Alert alert = new APropos();
        alert.showAndWait();
    }
}

