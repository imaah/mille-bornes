package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.cartes.Carte;

public class SmartBot extends Bot{
    private static final long serialVersionUID = -4106674790171584955L;

    public SmartBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
        return 0;
    }

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        return null;
    }
}
