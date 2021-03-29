package mille_bornes.cartes;

public class Borne extends Carte {
    public final int km;

    public Borne(int km) {
        super(km + "km")
        this.km = km;}

    void appliqueEffet(Jeu jeu, EtatJoueur etatjoueur) {};
}
