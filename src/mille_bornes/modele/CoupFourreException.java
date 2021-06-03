package mille_bornes.modele;

public class CoupFourreException extends RuntimeException {
    private final Joueur cible;

    public CoupFourreException(Joueur cible) {
        this.cible = cible;
    }

    public Joueur getCible() {
        return cible;
    }
}
