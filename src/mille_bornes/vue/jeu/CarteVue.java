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
    private static final double DEFAULT_SIZE = 140;
    protected boolean survolActif = true;
    private double ratio = 1.0;
    private CarteRotation rotation = CarteRotation.DEG_0;
    private boolean afficherSiNull = false;
    private boolean grisee = false;
    private Carte carte;
    private int index;

    /**
     * Créer une nouvelle carte, avec ou sans survol
     * @param carte La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index L'index que la carte prendra dans la main du joueur
     * @param survolActif true si le survol doit être actif
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index, boolean survolActif) {
        this(carte, milleBornes, index);
        setSurvolActif(survolActif);
    }

    /**
     * Créer une nouvelle carte, avec ou sans survol, avec ou sans grisement
     * @param carte La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index L'index que la carte prendra dans la main du joueur
     * @param survolActif true si le survol doit être actif
     * @param grisee true si la carte doit être grisée
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index, boolean survolActif, boolean grisee) {
        this(carte, milleBornes, index, survolActif);
        setGrisee(grisee);
    }

    /**
     * Créer un nouveau controlleur, défini le visuel de carte et l'indexe dans la main
     * @param carte La carte à créer
     * @param milleBornes Le jeu en cours
     * @param index L'index que la carte prendra dans la main du joueur
     */
    public CarteVue(Carte carte, MilleBornes milleBornes, int index) {
        new ControleurCarte(this, milleBornes);
        changeCarte(carte);
        this.index = index;
    }

    public void changeCarte(Carte carte) {
        double size = DEFAULT_SIZE * ratio;

        if (carte == null && afficherSiNull) {
            carte = DefaultCarte.VIDE;
        }

        this.carte = carte;
        Image image;
        setTranslateX(0);
        setTranslateY(0);
        setTranslateZ(-1);
        setScaleX(1);
        setScaleY(1);

        if (this.carte != null) {
            image = new Image(this.carte.getImagePath());
        } else {
            setHeight(0);
            setWidth(0);
            return;
        }

        double width = size * (image.getWidth() / image.getHeight());

        switch (rotation) {
            case DEG_0:
            case DEG_180:
                setWidth(width);
                setHeight(size);
                break;
            case DEG_90:
            case DEG_270:
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

    public void setRatio(double ratio) {
        this.ratio = ratio;
        this.changeCarte(carte);
    }

    public void tourner(CarteRotation rotation) {
        this.rotation = rotation;
        changeCarte(carte);
    }

    public void setSurvolActif(boolean survolActif) {
        this.survolActif = survolActif;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setAfficherSiNull(boolean afficherSiNull) {
        this.afficherSiNull = afficherSiNull;
    }

    public boolean estGrisee() {
        return grisee;
    }

    public boolean estSurvolActif() {
        return survolActif;
    }

    public double getRatio() {
        return ratio;
    }

    public CarteRotation getRotation() {
        return rotation;
    }

    /**
     * Permet de savoir l'index de la carte
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
        DEG_180(180),
        DEG_90(90),
        DEG_270(270);

        public final double angle;

        CarteRotation(double angle) {
            this.angle = angle;
        }
    }
}
