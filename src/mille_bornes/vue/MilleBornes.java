package mille_bornes.vue;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mille_bornes.controleur.BarreMenu;
import mille_bornes.controleur.Controleur;
import mille_bornes.modele.CoupFourreException;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.vue.jeu.Sabot;
import mille_bornes.vue.joueur.HJoueurMain;
import mille_bornes.vue.joueur.JoueurMain;
import mille_bornes.vue.joueur.VJoueurMain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MilleBornes {

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private final JoueurMain[] mains = new JoueurMain[4];
    private Jeu jeu;
    private final Controleur controleur = new Controleur(this);

    public MilleBornes(double width, double height) throws IOException {
        contenu = new BorderPane();
        sabot = new Sabot(null, this);

        FXMLLoader loader = new FXMLLoader(MilleBornes.class.getResource("/fxml/barre-menu.fxml"));
        MenuBar barreMenu = loader.load();
        Object controller = loader.getController();

        if (controller instanceof BarreMenu) {
            ((BarreMenu) controller).setGui(this);
        }

        vBox = new VBox();

        contenu.setBackground(new Background(
                new BackgroundFill(Color.rgb(158, 251, 144), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        vBox.getChildren().addAll(barreMenu, contenu);

        contenu.setPrefWidth(width);
        contenu.setPrefHeight(height);
        contenu.setPadding(new Insets(5, 5, 5, 5));
        contenu.setCenter(sabot);
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setJeu(Jeu jeu) {
        setJeu(jeu, false);
    }

    public void setJeu(Jeu jeu, boolean partieChargee) {
        this.jeu = jeu;
        if (!partieChargee) this.jeu.prepareJeu();
        sabot.setJeu(jeu);
        Arrays.fill(mains, null);
        mains[0] = new HJoueurMain(this, jeu.getJoueurs().get(0), true);

        // Selection manuelle des emplacements des joueurs selon le nombre total
        switch (jeu.getNbJoueurs()) {
            case 2:
                mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(1), false, true);
                break;
            case 3:
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false, true);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(2), false);
                break;
            case 4:
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false, true);
                mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(2), false, true);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(3), false);
                break;
        }

        for (int i = 1; i < mains.length; i++) {
            if (mains[i] != null) mains[i].cacher();
        }

        contenu.setBottom(mains[0]);
        contenu.setRight(mains[1]);
        contenu.setTop(mains[2]);
        contenu.setLeft(mains[3]);
        jeu.activeProchainJoueurEtTireCarte();
        tournerJoueurs();
    }

    public void tournerJoueurs() {
        Joueur joueur = jeu.getJoueurActif();

        mains[0].setJoueur(joueur);

        for (int i = 1; i < mains.length; i++) {
            if (mains[i] == null) continue;
            joueur = joueur.getProchainJoueur();
            mains[i].setJoueur(joueur);
        }
    }

    public Sabot getSabot() {
        return sabot;
    }

    public BorderPane getContenu() {
        return contenu;
    }

    public VBox getHolder() {
        return vBox;
    }

    public void defausseCarte(Carte carte) {
        try {
            jeu.getJoueurActif().defausseCarte(jeu, carte);

            if(testVictoire()) {
                controleur.demanderRejouer();
                return;
            }

            jeu.activeProchainJoueurEtTireCarte();
            sabot.update();
            tournerJoueurs();
        } catch (IllegalStateException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText("Vous ne pouvez pas faire cette action");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }

    public boolean testVictoire() {
        if (jeu.estPartieFinie()) {
            String content;
            List<Joueur> gagnants = jeu.getGagnant();

            if (gagnants.size() == 1) {
                content = gagnants.get(0).nom + " remporte la partie !";
            } else {
                content = "Le gagnant de cette partie sont : \n- " + gagnants.stream()
                        .map(Joueur::getNom).collect(Collectors.joining("\n- "));
            }

            Alert alert = genererAlert(
                    Alert.AlertType.INFORMATION,
                    "Fin de partie",
                    "Victoire !",
                    content);

            alert.showAndWait();

            return true;
        }

        return false;
    }

    private Alert genererAlert(Alert.AlertType type, String titre, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(type, content, buttons);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        return alert;
    }

    public void joueCarte(Carte carte) {
        try {
            if (carte instanceof Attaque) {
                // afficher alert
                Joueur cible = new ChoisitDestination(jeu, (Attaque) carte).getCible();

                if (cible == null) return;

                jeu.getJoueurActif().joueCarte(jeu, carte, cible);
            } else {
                jeu.getJoueurActif().joueCarte(jeu, carte);
            }

            if(testVictoire()) {
                controleur.demanderRejouer();
                return;
            }

            jeu.activeProchainJoueurEtTireCarte();
            sabot.update();
            tournerJoueurs();
        } catch (IllegalStateException e) {
            Alert error = genererAlert(Alert.AlertType.ERROR,
                    "Erreur", "Vous ne pouvez pas faire cette action.", e.getMessage());
            error.showAndWait();
        } catch (CoupFourreException e) {
            Alert coupFourre = genererAlert(Alert.AlertType.INFORMATION, "Coup Fourré !",
                    "Votre adversaire sort un coup-fourré !",
                    "Votre attaque n'a aucun effet et il récupère la main.");
            coupFourre.showAndWait();
            jeu.activeProchainJoueurEtTireCarte();
            sabot.update();
            tournerJoueurs();
        }
    }
}
