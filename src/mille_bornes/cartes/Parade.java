package mille_bornes.cartes;

public abstract class Parade extends Bataille {
    public Parade(String nom) {
        super(nom, Categorie.PARADE);
    }
}
