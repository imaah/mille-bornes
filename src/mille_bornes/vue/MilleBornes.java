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

public class MilleBornes extends StackPane {

    public static final long DUREE_ANIM_BASE = 1000L;

    private final BorderPane contenu;
    private final VBox vBox;
    private final Sabot sabot;
    private final JoueurMain[] mains = new JoueurMain[4];
    private final Controleur controleur = new Controleur(this);
    private final MessageText message = new MessageText();
    private Jeu jeu;


    /**
     * Constructeur - Permet d'initialiser tout les composants de la fenêtre
     *
     * @param width La largeur de la fenêtre
     * @param height La longueur de la fenêtre
     * @throws IOException Levée si le fichier .fxml n'a pas pu être ouvert
     */
    public MilleBornes(double width, double height) throws IOException {
        this.setWidth(width);
        this.setHeight(height);
        contenu = new BorderPane();
        sabot = new Sabot(null, this);

        // On charge la barre de menu
        FXMLLoader loader = new FXMLLoader(MilleBornes.class.getResource("/fxml/barre-menu.fxml"));
        MenuBar barreMenu = loader.load();
        Object controller = loader.getController();

        barreMenu.setTranslateZ(-2);

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

        getChildren().add(vBox);
        getChildren().add(message);

        // Il faut changer la position du message
        message.setX(getWidth() / 2);
        message.setY(getHeight() / 2);
        message.setTranslateZ(-2);
    }


    /**
     * Retourne le jeu en cours
     *
     * @return Le jeu en cours
     */
    public Jeu getJeu() {
        return jeu;
    }


    /**
     * Défini le jeu à utiliser
     *
     * @param jeu Le jeu à utiliser
     */
    public void setJeu(Jeu jeu) {
        setJeu(jeu, false);
    }


    /**
     * Permet de changer le jeu en précisant si ce jeu provient d'un fichier de sauvegarde
     *
     * @param jeu Le jeu à utiliser
     * @param partieChargee Indique si la partie provient d'un fichier de sauvegarde
     */
    public void setJeu(Jeu jeu, boolean partieChargee) {
        this.jeu = jeu;
        // Si la partie n'a pas été chargée, on le fait
        if (!partieChargee) this.jeu.prepareJeu();
        sabot.setJeu(jeu);

        // On remplit les mains
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

        // On cache les mains qui ne doivent pas être montrées
        for (int i = 1; i < mains.length; i++) {
            if (mains[i] != null) mains[i].cacher();
        }

        // On ajoute les différentes parties sur l'interface
        contenu.setBottom(mains[0]);
        contenu.setRight(mains[1]);
        contenu.setTop(mains[2]);
        contenu.setLeft(mains[3]);
        jeu.activeProchainJoueurEtTireCarte();
        tournerJoueurs();

        // Toutes les mains de bots doivent être cachées
        if (jeu.getJoueurActif() instanceof Bot) {
            mains[0].cacher();
            TimerUtils.wait(DUREE_ANIM_BASE, this::botJoue);
        }
    }


    /**
     * Permet d'actualiser les positions de chaque joueur
     */
    public void tournerJoueurs() {
        Joueur joueur = jeu.getJoueurActif();

        // Le joueur actif ne va plus l'être, son affichage change
        trouverMainDepuisJoueur(joueur).setNomGras(false);

        mains[0].setJoueur(joueur);

        // On décale chaque joueur dans la liste
        for (int i = 1; i < mains.length; i++) {
            if (mains[i] == null) continue;
            joueur = joueur.getProchainJoueur();
            mains[i].setJoueur(joueur);
        }

        // Le joueur actif obtient les propriétés dont il doit faire l'objet
        mains[0].setNomGras(true);
        mains[0].setSurvolActif(!(jeu.getJoueurActif() instanceof Bot));

        message.afficherMessage("Au tour de " + jeu.getJoueurActif().nom, 1000);
    }


    /**
     * Retourne le sabot du jeu
     * @return Le sabot du jeu
     */
    public Sabot getSabot() {
        return sabot;
    }


    /**
     * Retourne le contenu du jeu
     * @return Le contenu du jeu
     */
    public BorderPane getContenu() {
        return contenu;
    }


    /**
     * Retourne la VBox du jeu
     * @return La VBox du jeu
     */
    public VBox getHolder() {
        return vBox;
    }


    /**
     * Permet de défausser une carte précisée
     *
     * @param carte La carte à défausser
     */
    public void defausseCarte(CarteVue carte) {
        try { // On essaye de défausser la carte...
            jeu.getJoueurActif().defausseCarte(jeu, carte.getCarte());
            animerAction(carte, -(carte.getIndex() + 1), null);
        } catch (IllegalStateException e) { // ...sauf si on a pas le droit
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText("Vous ne pouvez pas faire cette action");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
    }


    /**
     * Permet de savoir si un ou plusieurs joueurs ont gagnés (et donc par extension que la partie est finie)
     *
     * @return True si la partie est finie, false sinon
     */
    public boolean testVictoire() {
        if (jeu.estPartieFinie()) {
            String content;
            List<Joueur> gagnants = jeu.getGagnant();

            for (JoueurMain main : mains) {
                if (main != null) main.montrer();
            }

            if (gagnants.size() == 1) {
                content = gagnants.get(0).nom + " remporte la partie !";
            } else {
                content = "Le gagnant de cette partie sont : \n- " + gagnants.stream()
                        .map(Joueur::getNom).collect(Collectors.joining("\n- "));
            }

            // On affiche les gagnants dans une boîte de dialogue
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


    /**
     * Permet de générer une alerte avec un message personnalisé
     *
     * @param type Le type de l'alert (utile pour l'icone et les boutons)
     * @param titre Le titre de la fenêtre
     * @param header Le contenu de la section en-tête
     * @param content Le texte central de la fenêtre
     * @param buttons Les boutons qui peuvent être ajoutés à la fenêtre (optionel)
     * @return La boîte de dialogue personnalisée
     */
    private Alert genererAlert(Alert.AlertType type, String titre, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(type, content, buttons);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        return alert;
    }


    /**
     * Permet de finir un tour correctement et de passer au prochain
     */
    private void finDeTour() {
        // Si la partie n'est pas finie
        if (testVictoire()) {
            controleur.demanderRejouer();
            return;
        }

        jeu.activeProchainJoueurEtTireCarte();
        sabot.update();

        // Alors on tourne les joueurs
        tournerJoueurs();
        if (jeu.getJoueurActif() instanceof Bot) {
            mains[0].cacher();
            TimerUtils.wait((long) (DUREE_ANIM_BASE / 3d), this::botJoue);
            return;
        }
        mains[0].montrer();
    }


    /**
     * Permet de faire jouer un bot quand c'est à lui de jouer
     */
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

        // Il essaye de jouer toutes les cartes possibles une fois, jusqu'à ce qu'il en trouve une (ou défausse)
        do {
            try {
                // On choisit une carte parmis les choix restants
                nCarte = bot.choisitCarte();
                carte = bot.getMain().get(Math.abs(nCarte) - 1);

                // On traite ce qu'il a choisit
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
            } catch (IllegalStateException e) { // Parfois, il prend une carte qu'il ne peut pas jouer
                System.out.println(e.getMessage());
                carteJouee = false;
            } catch (CoupFourreException e) {
                Alert coupFourre = genererAlert(Alert.AlertType.INFORMATION, "Coup Fourré !",
                        "Votre adversaire sort un coup-fourré !",
                        "L'attaque de " + bot.nom + " n'a aucun effet et " + e.getCible().nom + " récupère la main.");
                coupFourre.showAndWait();
                carteJouee = true;
            }

        } while (!carteJouee); // On doit repéter ces actions tant qu'il n'a pas pu jouer

        // Animation
        CarteVue vue = mains[0].getCartes()[Math.abs(nCarte) - 1];
        if (nCarte < 0) {
            animerAction(vue, nCarte, null);
        } else {
            animerAction(vue, nCarte, cible);
        }
    }


    /**
     * Permet de gérer les différents cas d'animation entre les joueurs
     *
     * @param vue La carte à animer
     * @param nCarte Sa position dans la main
     * @param cible Sa destination
     */
    private void animerAction(CarteVue vue, int nCarte, Joueur cible) {
        mains[0].montrer(Math.abs(nCarte) - 1);
        Carte carte = vue.getCarte();
        CarteVue destination;

        System.out.println(cible);
        System.out.println(carte);

        // On traite au cas-par-cas
        if (nCarte < 0) {
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

        // Enfin, on genére l'animation
        Animation animation = CarteTransition.getCombinedTransition(vue, destination);
        animation.setOnFinished(e -> onAnimationFinish(destination, vue.getCarte()));
        animation.playFromStart();
    }


    /**
     * Remplace la carte de destination et attend un temps après la fin d'une animation pour éviter les bugs d'animation
     * @param cible la carte à remplacer
     * @param carte le remplacement de la carte
     */
    private void onAnimationFinish(CarteVue cible, Carte carte) {
        cible.changeCarte(carte);
        onAnimationFinish();
    }

    /**
     * Attend un temps après la fin d'une animation pour éviter les bugs d'animation
     */
    private void onAnimationFinish() {
        TimerUtils.wait(DUREE_ANIM_BASE / 2, this::finDeTour);
    }


    /**
     * Permet de trouver la main d'un joueur
     *
     * @param joueur Le joueur recherché
     * @return La main du joueur
     */
    private JoueurMain trouverMainDepuisJoueur(Joueur joueur) {
        for (JoueurMain main : mains) {
            if (main != null && main.getJoueur().equals(joueur)) return main;
        }
        throw new IllegalStateException("joueur inconnu");
    }


    /**
     * Permet de jouer une carte
     *
     * @param vue La carte à jouer
     */
    public void joueCarte(CarteVue vue) {
        Carte carte = vue.getCarte();
        try {
            if (carte instanceof Attaque) {
                // Afficher alert
                Joueur cible = new ChoisitDestination(jeu, (Attaque) carte).getCible();

                if (cible == null) return;

                jeu.getJoueurActif().joueCarte(jeu, carte, cible);
                animerAction(vue, vue.getIndex() + 1, cible);
            } else {
                jeu.getJoueurActif().joueCarte(jeu, carte);
                animerAction(vue, vue.getIndex() + 1, null);
            }
        } catch (IllegalStateException e) { // Si on a pas le droit de faire ce déplacement
            Alert error = genererAlert(Alert.AlertType.ERROR,
                    "Erreur", "Vous ne pouvez pas faire cette action.", e.getMessage());
            error.showAndWait();
        } catch (CoupFourreException e) { // Si le joueur cachait un coup-fourré, on le signale
            Alert coupFourre = genererAlert(Alert.AlertType.INFORMATION, "Coup Fourré !",
                    "Votre adversaire sort un coup-fourré !",
                    "Votre attaque n'a aucun effet et " + e.getCible().nom + " récupère la main.");
            coupFourre.showAndWait();

            // Une fois la boîte de dialogue fermée, on continu le jeu
            jeu.activeProchainJoueurEtTireCarte();
            sabot.update();
            tournerJoueurs();
        }
    }
}
