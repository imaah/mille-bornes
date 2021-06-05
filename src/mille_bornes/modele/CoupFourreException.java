package mille_bornes.modele;

public class CoupFourreException extends RuntimeException {
    /** La cible en question */
    private final Joueur cible;

    /**
     * Permet de préciser une cible à viser
     *
     * @param cible La cible en question
     */
    public CoupFourreException(Joueur cible) {
        this.cible = cible;
    }
}
