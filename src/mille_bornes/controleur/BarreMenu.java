package mille_bornes.controleur;

import javafx.scene.control.Alert;
import mille_bornes.vue.APropos;
import mille_bornes.vue.MilleBornes;

/**
 *
 */
public class BarreMenu {
    private Controleur controleur;

    /**
     * Permet de créer une nouvelle partie avec des options
     */
    public void onCreerNouvellePartie() {
        controleur.nouvellePartie();
    }


    /**
     * Permet de charger sur l'interface avec la partie actuelle
     * @param gui
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
        String message = "Vous êtes sur le point de fermer l'application. Êtes-vous sûr de vouloir quitter ?";
        if (controleur.dejaUnePartieEnCours()) message += " Comme une partie est déjà en cours, la fermeture de" +
                                                          " l'application sans sauvegarde entrainera la perte de" +
                                                          " l'état de la partie!";
        if (controleur.confirmation("Quitter",
                "Voulez-vous vraiment quitter ?",
                message
        )) System.exit(0);
    }

    /**
     * Créé un nouvel objet APropos pour afficher la fenêtre
     */
    public void onAPropos() {
        Alert alert = new APropos();
        alert.showAndWait();
    }
}

