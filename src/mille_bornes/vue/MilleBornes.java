package mille_bornes.vue;

import javafx.animation.Animation;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mille_bornes.controleur.BarreMenu;
import mille_bornes.controleur.Controleur;
import mille_bornes.modele.CoupFourreException;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.*;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.modele.cartes.parades.FinDeLimite;
import mille_bornes.modele.extensions.bots.Bot;
import mille_bornes.vue.animation.CarteTransition;
import mille_bornes.vue.jeu.CarteVue;
import mille_bornes.vue.jeu.Sabot;
import mille_bornes.vue.joueur.HJoueurMain;
import mille_bornes.vue.joueur.JoueurMain;
import mille_bornes.vue.joueur.VJoueurMain;
import mille_bornes.vue.timer.TimerUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MilleBornes {

    public static final long DUREE_ANIM_BASE = 1000L;

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private final JoueurMain[] mains = new JoueurMain[4];
    private final Controleur controleur = new Controleur(this);
    private Jeu jeu;

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
            case 2 -> mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(1), false, true);
            case 3 -> {
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false, true);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(2), false);
            }
            case 4 -> {
                mains[1] = new VJoueurMain(this, jeu.getJoueurs().get(1), false, true);
                mains[2] = new HJoueurMain(this, jeu.getJoueurs().get(2), false, true);
                mains[3] = new VJoueurMain(this, jeu.getJoueurs().get(3), false);
            }
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

        if (jeu.getJoueurActif() instanceof Bot) {
            mains[0].cacher();
            TimerUtils.wait(DUREE_ANIM_BASE, this::botJoue);
        }
    }

    public void tournerJoueurs() {
        Joueur joueur = jeu.getJoueurActif();

        mains[0].setJoueur(joueur);

        for (int i = 1; i < mains.length; i++) {
            if (mains[i] == null) continue;
            joueur = joueur.getProchainJoueur();
            mains[i].setJoueur(joueur);
        }

        mains[0].setSurvolActif(!(jeu.getJoueurActif() instanceof Bot));
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

    public void defausseCarte(CarteVue carte) {
        try {
            jeu.getJoueurActif().defausseCarte(jeu, carte.getCarte());
            animerAction(carte, -carte.getIndex(), null);
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
                    content
            );

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

    private void finDeTour() {
        if (testVictoire()) {
            controleur.demanderRejouer();
            return;
        }

        jeu.activeProchainJoueurEtTireCarte();
        sabot.update();

        tournerJoueurs();
        if (jeu.getJoueurActif() instanceof Bot) {
            mains[0].cacher();
            TimerUtils.wait((long) (DUREE_ANIM_BASE / 3d), this::botJoue);
            return;
        }
        mains[0].montrer();
    }

    private void botJoue() {
        boolean carteJouee;
        if (!(jeu.getJoueurActif() instanceof Bot)) {
            throw new IllegalStateException("Le joueur Actif n'est pas de type Bot");
        }
        System.out.println("------------------------");
        mains[0].cacher();
        Carte carte;
        int nCarte = 0;
        Joueur cible = null;

        Bot bot = (Bot) jeu.getJoueurActif();

        do {
            try {
                nCarte = bot.choisitCarte();
                carte = bot.getMain().get(Math.abs(nCarte) - 1);

                if (nCarte > 0) {
                    if (carte instanceof Attaque) {
                        cible = bot.choisitAdversaire(carte);
                        bot.joueCarte(jeu, nCarte - 1, cible);
                        System.out.println("cible : " + cible);
                    } else {
                        bot.joueCarte(jeu, nCarte - 1);
                    }
                } else if (nCarte < 0) {
                    bot.defausseCarte(jeu, Math.abs(nCarte) - 1);
                } else {
                    throw new IllegalStateException("Entrez un numéro de carte entre 1 et 7 inclus (négatif pour défausser)");
                }
                carteJouee = true;
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                carteJouee = false;
            } catch (CoupFourreException e) {
                carteJouee = true;
            }

        } while (!carteJouee);

        // animation
        CarteVue vue = mains[0].getCartes()[Math.abs(nCarte) - 1];
        if (nCarte < 0) {
            animerAction(vue, -(Math.abs(nCarte) - 1), null);
        } else {
            animerAction(vue, nCarte - 1, cible);
        }
    }

    private void animerAction(CarteVue vue, int nCarte, Joueur cible) {
        mains[0].montrer(Math.abs(nCarte));
        Carte carte = vue.getCarte();
        CarteVue destination;

        System.out.println(cible);
        System.out.println(carte);
        if (nCarte <= 0) {
            destination = sabot.getDefausse();
        } else if (carte instanceof LimiteVitesse) {
            destination = trouverMainDepuisJoueur(cible).getLimite();
        } else if (carte instanceof Attaque) {
            destination = trouverMainDepuisJoueur(cible).getBataille();
        } else if (carte instanceof FinDeLimite) {
            destination = mains[0].getLimite();
        } else if (carte instanceof Parade) {
            destination = mains[0].getBataille();
        } else if (carte instanceof Botte) {
            destination = mains[0].getBotte(carte.getClass().asSubclass(Botte.class));
        } else if (carte instanceof Borne) {
            Animation animation = CarteTransition.getBorneAnimation(vue, Duration.millis(DUREE_ANIM_BASE / 3d));
            animation.setOnFinished(e -> onAnimationFinish());
            animation.playFromStart();
            return;
        } else {
            finDeTour();
            return;
        }

        Animation animation = CarteTransition.getCombinedTransition(vue, destination);
        animation.setOnFinished(e -> onAnimationFinish());
        animation.playFromStart();
    }

    private void onAnimationFinish() {
        TimerUtils.wait(DUREE_ANIM_BASE / 2, this::finDeTour);
    }

    private JoueurMain trouverMainDepuisJoueur(Joueur joueur) {
        for (JoueurMain main : mains) {
            if (main != null && main.getJoueur().equals(joueur)) return main;
        }
        throw new IllegalStateException("joueur inconnu");
    }

    public void joueCarte(CarteVue vue) {
        Carte carte = vue.getCarte();
        try {
            if (carte instanceof Attaque) {
                // afficher alert
                Joueur cible = new ChoisitDestination(jeu, (Attaque) carte).getCible();

                if (cible == null) return;

                jeu.getJoueurActif().joueCarte(jeu, carte, cible);
                animerAction(vue, vue.getIndex(), cible);
            } else {
                jeu.getJoueurActif().joueCarte(jeu, carte);
                animerAction(vue, vue.getIndex(), null);
            }
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
