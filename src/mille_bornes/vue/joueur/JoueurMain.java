package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Botte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.modele.cartes.bottes.AsDuVolant;
import mille_bornes.modele.cartes.bottes.Citerne;
import mille_bornes.modele.cartes.bottes.Increvable;
import mille_bornes.modele.cartes.bottes.VehiculePrioritaire;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.Updatable;
import mille_bornes.vue.jeu.BotteVue;
import mille_bornes.vue.jeu.CarteVue;

public abstract class JoueurMain extends GridPane implements Updatable {
    protected final CarteVue[] cartes = new CarteVue[7];
    protected final Label statusLabel = new Label();
    protected final CarteVue limite;
    protected final CarteVue bataille;
    protected final BotteVue[] bottes = new BotteVue[4];
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

        bottes[0] = new BotteVue(new VehiculePrioritaire(), milleBornes, true);
        bottes[1] = new BotteVue(new AsDuVolant(), milleBornes, true);
        bottes[2] = new BotteVue(new Citerne(), milleBornes, true);
        bottes[3] = new BotteVue(new Increvable(), milleBornes, true);

        for (BotteVue botte : bottes) {
            botte.setRatio(.7);
            botte.setAfficherSiNull(true);
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

        for (BotteVue botteVue : bottes) {
            botteVue.setGrisee(joueur.getBottes().contains((Botte) botteVue.getCarte()));
        }

        if (cacher) {
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
