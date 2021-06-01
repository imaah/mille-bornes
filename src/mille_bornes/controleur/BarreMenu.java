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
        // Choix du nombre de joueurs
        controleur.nouvellePartie();
    }

    public void setGui(MilleBornes gui) {
        this.controleur = new Controleur(gui);
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
     * Demande confirmation avant de quitter l'application
     */
    public void onQuitter() {
        if (controleur.confirmation("Quitter",
                "Voulez-vous vraiment quitter ?",
                "Vous êtes sur le point de quitter l'application. Si vous confirmez, la partie en cours ne sera pas sauvegardée!"
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

