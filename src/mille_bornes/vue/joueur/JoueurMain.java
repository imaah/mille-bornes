package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Botte;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.modele.cartes.bottes.AsDuVolant;
import mille_bornes.modele.cartes.bottes.Citerne;
import mille_bornes.modele.cartes.bottes.Increvable;
import mille_bornes.modele.cartes.bottes.VehiculePrioritaire;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.Updatable;
import mille_bornes.vue.jeu.CarteVue;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite permettant de gérer les différentes apparences des mains et statuts des joueurs.
 * @see HJoueurMain
 * @see VJoueurMain
 */
public abstract class JoueurMain extends GridPane implements Updatable {
    /** Les vues des cartes de la main */
    protected final CarteVue[] cartes = new CarteVue[7];
    /** Le label de résumé */
    protected final Label statusLabel = new Label();
    /** La carte de limite */
    protected final CarteVue limite;
    /** La carte sur le haut de la bataille */
    protected final CarteVue bataille;
    /** La liste de bottes */
    protected final CarteVue[] bottes = new CarteVue[4];
    /** Les cartes de la main */
    private List<Carte> cartesJoueur;
    /** Le joueur qui les possèdes */
    private Joueur joueur;
    /** S'il faut cacher les cartes (quand le joueur ne joue pas) */
    private boolean cacher = false;
    /** Le tooltip à montrer au survol des bottes non possédées */
    Tooltip botteVueNonPossedeeTooltip = new Tooltip("Vous ne possédez pas cette botte!");
    /** Le tooltip à montrer au survol des bottes non possédées */
    Tooltip botteVuePossedeeTooltip = new Tooltip("Vous possédez cette botte!");

    /**
     * Permet de créer une main de joueur
     *
     * @param milleBornes Le milleBornes ou le faire
     * @param joueur Le joueur à qui créer la main
     * @param survolActif S'il faut activer le survol
     */
    public JoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this.joueur = joueur;
        limite = new CarteVue(null, milleBornes, -1, false);
        bataille = new CarteVue(null, milleBornes, -1, false);
        cartesJoueur = new ArrayList<>(joueur.getMain());

        setAlignment(Pos.CENTER);

        setHgap(3);
        setVgap(3);

        // On créer les vues de chaque carte
        for (int i = 0; i < cartes.length; i++) {
            if (i < joueur.getMain().size()) {
                cartes[i] = new CarteVue(joueur.getMain().get(i), milleBornes, i, survolActif);
            } else {
                cartes[i] = new CarteVue(null, milleBornes, i, survolActif);
            }
            cartes[i].setRatio(.7);
        }

        // On traite chaque botte séparément
        bottes[0] = new CarteVue(new VehiculePrioritaire(), milleBornes, 0, false, true);
        bottes[1] = new CarteVue(new AsDuVolant(), milleBornes, 1, false, true);
        bottes[2] = new CarteVue(new Citerne(), milleBornes, 2, false, true);
        bottes[3] = new CarteVue(new Increvable(), milleBornes, 3, false, true);

        // Les tailles des cartes
        for (CarteVue botte : bottes) {
            botte.setRatio(.7);
            botte.setAfficherSiNull(true);
        }

        botteVuePossedeeTooltip.setShowDelay(Duration.millis(200));
        botteVueNonPossedeeTooltip.setShowDelay(Duration.millis(200));
        limite.setRatio(.7);
        bataille.setRatio(.7);
        limite.setAfficherSiNull(true);
        bataille.setAfficherSiNull(true);
        update();
    }

    /**
     * Permet d'actualiser la main d'un joueur
     */
    public void update() {
        cartesJoueur = new ArrayList<>(joueur.getMain());
        for (int i = 0; i < cartes.length; i++) { // On vérifie chaque carte
            if (i < joueur
                    .getMain()
                    .size()) {
                cartes[i].changeCarte(cartesJoueur.get(i));
            } else {
                cartes[i].changeCarte(null);
            }
        }

        // Les bottes aussi
        for (CarteVue botteVue : bottes) {
            boolean hasBotte = joueur.getBottes().contains(botteVue.getCarte());
            botteVue.setGrisee(!hasBotte);

            // On supprime systématiquement les hintbox
            Tooltip.uninstall(botteVue, botteVueNonPossedeeTooltip);
            Tooltip.uninstall(botteVue, botteVuePossedeeTooltip);

            // Message explicatif au survol, en fonction de si on possède la botte ou pas
            if (!hasBotte) {
                Tooltip.install(
                        botteVue,
                        botteVueNonPossedeeTooltip
                );
            } else {
                Tooltip.install(
                        botteVue,
                        botteVuePossedeeTooltip
                );
            }
        }

        // MàJ de l'affichage
        if (cacher) {
            cacher();
        }
        updateLabel();
        bataille.changeCarte(joueur.getBataille());

        // Affichage de la limite de vitesse
        if (joueur.getLimiteVitesse()) {
            limite.changeCarte(new LimiteVitesse());
        } else {
            limite.changeCarte(null);
        }
    }

    /**
     * Permet de récupérer les vues des cartes
     *
     * @return Les vues
     */
    public CarteVue[] getCartes() {
        return cartes;
    }

    /**
     * Permet de cacher les cartes
     */
    public void cacher() {
        this.cacher = true;
        for (CarteVue vue : cartes) { // On les parcours toutes
            if (vue.getCarte() == null) continue;
            vue.changeCarte(DefaultCarte.DEFAULT);
        }
    }

    /**
     * Permet de montrer les cartes
     */
    public void montrer() {
        this.cacher = false;
        setSurvolActif(true);
        update();
    }

    /**
     * Permet de montrer une carte spécifique
     *
     * @param i L'index de la carte à montrer
     */
    public void montrer(int i) {
        cartes[i].changeCarte(cartesJoueur.get(i));
    }

    /**
     * Permet de récupérer le joueur
     *
     * @return Le joueur
     */
    public Joueur getJoueur() {
        return joueur;
    }

    /**
     * Permet de changer le joueur
     *
     * @param joueur Le joueur à mettre
     */
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        update();
    }

    /**
     * Permet de retourner la vue de la carte sur le haut de la pille de bataille
     *
     * @return La vue
     */
    public CarteVue getBataille() {
        return bataille;
    }

    /**
     * Permet de changer le survol d'une carte
     *
     * @param survolActif true si le survol doit devenir actif, false sinon
     */
    public void setSurvolActif(boolean survolActif) {
        for (CarteVue carteVue : cartes) {
            carteVue.setSurvolActif(survolActif);
        }
    }

    /**
     * Permet de mettre le nom d'un joueur en gras
     *
     * @param estGras true s'il doit devenir gras, false sinon
     */
    public void setNomGras(boolean estGras) {
        if (estGras) {
            this.statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        } else {
            this.statusLabel.setStyle("-fx-font-weight: normal; -fx-font-size: 12px;");
        }
    }

    /**
     * Permet de récupérer la vue de la carte de limite de vitesse
     *
     * @return La vue de la carte
     */
    public CarteVue getLimite() {
        return limite;
    }

    /**
     * Permet de récupérer les vues des bottes
     *
     * @return Un tableau de vues des bottes
     */
    public CarteVue[] getBottes() {
        return bottes;
    }

    /**
     * Permet de faire des opérations à partir d'une botte si un joueur la possède
     *
     * @param aClass La botte recherchée
     * @return La botte si le joueur l'a, null sinon
     */
    public CarteVue getBotte(Class<? extends Botte> aClass) {
        for (CarteVue botte : bottes) {
            if (botte.getCarte().getClass().equals(aClass)) {
                return botte;
            }
        }
        return null;
    }

    /**
     * Permet de mettre à jour le label de résumé du joueur
     */
    public void updateLabel() {
        statusLabel.setText(String.format("%s%n%d km", joueur.nom, joueur.getKm()));
    }
}
