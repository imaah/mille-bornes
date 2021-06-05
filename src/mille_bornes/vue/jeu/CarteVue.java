package mille_bornes.vue.jeu;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import mille_bornes.controleur.ControleurCarte;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.vue.MilleBornes;

public class CarteVue extends Rectangle {
    /** Taille par défaut d'une carte */
    private static final double DEFAULT_SIZE = 140;
    /** Survol actif par défaut */
    protected boolean survolActif = true;
    /** Taille d'une carte par défaut */
    private double ratio = 1.0;
    /** Angle: par défaut droit */
    private CarteRotation rotation = CarteRotation.DEG_0;
    /** Si on doit afficher la carte quand elle n'est pas définie */
    private boolean afficherSiNull = false;
    /** Si la carte est grisée (utilisé pour les bottes) */
    private boolean grisee = false;
    /** La carte en question */
    private Carte carte;
    /** La position de la carte dans la main */
    private final int index;

    /**
     * Créer une nouvelle carte, avec ou sans survol
     *
     * @param carte       La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index       L'index que la carte prendra dans la main du joueur
     * @param survolActif true si le survol doit être actif
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index, boolean survolActif) {
        this(carte, milleBornes, index);
        setSurvolActif(survolActif);
    }

    /**
     * Créer une nouvelle carte, avec ou sans survol, avec ou sans grisement
     *
     * @param carte       La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index       L'index que la carte prendra dans la main du joueur
     * @param survolActif true si le survol doit être actif
     * @param grisee      true si la carte doit être grisée
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index, boolean survolActif, boolean grisee) {
        this(carte, milleBornes, index, survolActif);
        setGrisee(grisee);
    }

    /**
     * Créer un nouveau controlleur, défini le visuel de carte et l'indexe dans la main
     *
     * @param carte       La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index       L'index que la carte prendra dans la main du joueur
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index) {
        new ControleurCarte(this, milleBornes);
        changeCarte(carte);
        this.index = index;
    }

    /**
     * Permet de changer le visuel d'une carte
     *
     * @param carte Le nouveau visuel à affecter
     */
    public void changeCarte(Carte carte) {
        // Changement potentiel de taille
        double size = DEFAULT_SIZE * ratio;

        if (carte == null && afficherSiNull) {
            carte = DefaultCarte.VIDE;
        }

        this.carte = carte;
        Image image;
        setTranslateX(0);
        setTranslateY(0);
        setTranslateZ(-2);
        setScaleX(1);
        setScaleY(1);

        // On change son image
        if (this.carte != null) {
            image = new Image(this.carte.getImagePath());
        } else {
            setHeight(0);
            setWidth(0);
            return;
        }

        double width = size * (image.getWidth() / image.getHeight());

        // Si la carte est horizontale, on l'affiche "normalement", sinon on inverse sa largeur et sa longueur
        switch (rotation) {
            case DEG_0: case DEG_180:
                setWidth(width);
                setHeight(size);
                break;
            case DEG_90: case DEG_270:
                setWidth(size);
                setHeight(width);
                break;
        }

        setRotate(0.0);

        ImageView view = new ImageView(image);
        view.setRotate(rotation.angle);
        setGrisee(grisee);
        ImagePattern pattern = new ImagePattern(view.snapshot(new SnapshotParameters(), null));
        setFill(pattern);

        setArcHeight(10);
        setArcWidth(10);
    }

    /**
     * Permet de griser ou dégriser la carte
     *
     * @param grisee true si on veut griser la carte, false sinon
     */
    public void setGrisee(boolean grisee) {
        this.grisee = grisee;
        if (this.grisee) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setSaturation(-.9);
            setEffect(adjust);
        } else {
            setEffect(null);
        }
    }

    /**
     * Permet de tourner une carte d'une certaine orientation
     *
     * @param rotation L'orientation à appliquer. Provient de CarteRotation
     */
    public void tourner(CarteRotation rotation) {
        this.rotation = rotation;
        changeCarte(carte);
    }

    /**
     * Permet d'activer ou de désactiver le survol
     *
     * @param survolActif true si le survol doit être actif, false sinon
     */
    public void setSurvolActif(boolean survolActif) {
        this.survolActif = survolActif;
    }

    /**
     * Retourne la carte
     *
     * @return La carte
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     * Précise si la carte doit être affichée même si elle est nulle
     *
     * @param afficherSiNull true si elle doit l'être, false sinon
     */
    public void setAfficherSiNull(boolean afficherSiNull) {
        this.afficherSiNull = afficherSiNull;
    }

    /**
     * Permet de savoir si une carte est grisée
     *
     * @return true si elle est grisée, false sinon
     */
    public boolean estGrisee() {
        return grisee;
    }

    /**
     * Permet de savoir si le survol est actif
     *
     * @return true s'il l'est, false sinon
     */
    public boolean estSurvolActif() {
        return survolActif;
    }

    /**
     * Permet de connaître le ratio de la carte
     *
     * @return Le ratio de la carte
     */
    public double getRatio() {
        return ratio;
    }

    /**
     * Permet de définir le ratio de la carte (sa taille en quelque sorte)
     *
     * @param ratio Le ratio de la carte
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
        this.changeCarte(carte);
    }

    /**
     * Permet de connaître la rotation de la carte
     *
     * @return La rotation de la carte. Provient de RotationCarte
     */
    public CarteRotation getRotation() {
        return rotation;
    }

    /**
     * Permet de savoir l'index de la carte
     *
     * @return L'index de la carte
     */
    public int getIndex() {
        return index;
    }

    /**
     * Permet d'appliquer des rotations aux cartes
     */
    public enum CarteRotation {
        DEG_0(0),
        DEG_90(90),
        DEG_180(180),
        DEG_270(270);

        public final double angle;

        CarteRotation(double angle) {
            this.angle = angle;
        }
    }
}
