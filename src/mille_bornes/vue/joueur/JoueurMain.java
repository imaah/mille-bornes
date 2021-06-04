package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mille_bornes.modele.Joueur;
import mille_bornes.modele.cartes.Botte;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.modele.cartes.bottes.AsDuVolant;
import mille_bornes.modele.cartes.bottes.Citerne;
import mille_bornes.modele.cartes.bottes.Increvable;
import mille_bornes.modele.cartes.bottes.VehiculePrioritaire;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.Updatable;
import mille_bornes.vue.jeu.CarteVue;

import java.util.ArrayList;
import java.util.List;

public abstract class JoueurMain extends GridPane implements Updatable {
    protected final CarteVue[] cartes = new CarteVue[7];
    protected final Label statusLabel = new Label();
    protected final CarteVue limite;
    protected final CarteVue bataille;
    protected final CarteVue[] bottes = new CarteVue[4];
    private List<Carte> cartesJoueur;
    private Joueur joueur;
    private boolean cacher = false;

    public JoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this.joueur = joueur;
        limite = new CarteVue(null, milleBornes, -1, false);
        bataille = new CarteVue(null, milleBornes, -1, false);
        cartesJoueur = new ArrayList<>(joueur.getMain());

        setAlignment(Pos.CENTER);

        setHgap(3);
        setVgap(3);

        for (int i = 0; i < cartes.length; i++) {
            if (i < joueur.getMain().size()) {
                cartes[i] = new CarteVue(joueur.getMain().get(i), milleBornes, i, survolActif);
            } else {
                cartes[i] = new CarteVue(null, milleBornes, i, survolActif);
            }
            cartes[i].setRatio(.7);
        }

        bottes[0] = new CarteVue(new VehiculePrioritaire(), milleBornes, 0, false, true);
        bottes[1] = new CarteVue(new AsDuVolant(), milleBornes, 1, false, true);
        bottes[2] = new CarteVue(new Citerne(), milleBornes, 2, false, true);
        bottes[3] = new CarteVue(new Increvable(), milleBornes, 3, false, true);

        for (CarteVue botte : bottes) {
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
        cartesJoueur = new ArrayList<>(joueur.getMain());
        for (int i = 0; i < cartes.length; i++) {
            if (i < joueur
                    .getMain()
                    .size()) {
                cartes[i].changeCarte(cartesJoueur.get(i));
            } else {
                cartes[i].changeCarte(null);
            }
        }

        for (CarteVue botteVue : bottes) {
            boolean hasBotte = joueur.getBottes().contains((Botte) botteVue.getCarte());
            botteVue.setGrisee(!hasBotte);

            if (!hasBotte) {
                Tooltip.install(
                        botteVue,
                        new Tooltip("Vous ne possedez pas cette botte")
                );
            }
        }

        if (cacher) {
            cacher();
        }
        updateLabel();
        bataille.changeCarte(joueur.getBataille());

        if (joueur.getLimiteVitesse()) {
            limite.changeCarte(new LimiteVitesse());
        } else {
            limite.changeCarte(null);
        }
    }

    public CarteVue[] getCartes() {
        return cartes;
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

    public void montrer(int i) {
        cartes[i].changeCarte(cartesJoueur.get(i));
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        update();
    }

    public CarteVue getBataille() {
        return bataille;
    }

    public void setSurvolActif(boolean survolActif) {
        for (CarteVue carteVue : cartes) {
            carteVue.setSurvolActif(survolActif);
        }
    }

    public void setNomGras(boolean estGras) {
//        Font font = this.statusLabel.getFont();
        if (estGras) {
            this.statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        } else {
            this.statusLabel.setStyle("-fx-font-weight: normal; -fx-font-size: 12px;");
        }
    }

    public CarteVue getLimite() {
        return limite;
    }

    public CarteVue[] getBottes() {
        return bottes;
    }

    public CarteVue getBotte(Class<? extends Botte> aClass) {
        for (CarteVue botte : bottes) {
            if (botte.getCarte().getClass().equals(aClass)) {
                return botte;
            }
        }
        return null;
    }

    public void updateLabel() {
        statusLabel.setText(String.format("%s%n%d km", joueur.nom, joueur.getKm()));
    }
}
