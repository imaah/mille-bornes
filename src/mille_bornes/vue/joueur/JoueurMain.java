package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mille_bornes.modele.Joueur;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;
import mille_bornes.vue.Updatable;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;

public abstract class JoueurMain extends GridPane implements Updatable {
    protected final CarteVue[] cartes = new CarteVue[7];
    protected final Label statusLabel = new Label();
    protected final CarteVue limite;
    protected final CarteVue bataille;
    protected final CarteVue[] bottes = new CarteVue[4];
    private Joueur joueur;
    private boolean cacher = false;

    public JoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this.joueur = joueur;
        limite = new CarteVue(null, milleBornes, false);
        bataille = new CarteVue(null, milleBornes, false);

        setAlignment(Pos.CENTER);

        setHgap(3);
        setVgap(3);

        for (int i = 0; i < cartes.length; i++) {
            if (i < joueur.getMain().size()) {
                cartes[i] = new CarteVue(joueur.getMain().get(i), milleBornes, survolActif);
            } else {
                cartes[i] = new CarteVue(null, milleBornes, survolActif);
            }
            cartes[i].setRatio(.7);
        }

        for(int i = 0; i < bottes.length; i++) {
            if (i < joueur.getBottes().size()) {
                bottes[i] = new CarteVue(joueur.getBottes().get(i), milleBornes, false);
            } else {
                bottes[i] = new CarteVue(null, milleBornes, false);
            }
            bottes[i].setRatio(.7);
            bottes[i].setAfficherSiNull(true);
        }

        limite.setRatio(.7);
        bataille.setRatio(.7);
        limite.setAfficherSiNull(true);
        bataille.setAfficherSiNull(true);
        update();
    }

    public void update() {
        for (int i = 0; i < cartes.length; i++) {
            if (i < joueur
                    .getMain()
                    .size()) {
                cartes[i].changeCarte(joueur.getMain().get(i));
            } else {
                cartes[i].changeCarte(null);
            }
        }

        for (int i = 0; i < bottes.length; i++) {
            if (i < joueur.getBottes().size()) {
                bottes[i].changeCarte(joueur.getBottes().get(i));
            } else {
                bottes[i].changeCarte(null);
            }
        }

        if(cacher) {
            cacher();
        }

        statusLabel.setText(String.format("%s\n%d km", joueur.nom, joueur.getKm()));
        bataille.changeCarte(joueur.getBataille());

        if (joueur.getLimiteVitesse()) {
            limite.changeCarte(new LimiteVitesse());
        } else {
            limite.changeCarte(null);
        }
    }

    public void cacher() {
        cacher = true;
        for (CarteVue vue : cartes) {
            if (vue.getCarte() == null) continue;
            vue.changeCarte(DefaultCarte.DEFAULT);
        }
    }

    public void montrer() {
        cacher = false;
        update();
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        update();
    }
}
