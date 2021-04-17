package mille_bornes.cartes;

public abstract class Botte extends Carte {
    public Botte(String nom) {
        super("Botte " + nom, Categorie.BOTTE);
    }

    public abstract boolean contre(Attaque carte);
}
