package mille_bornes.vue.jeu;

import javafx.scene.effect.ColorAdjust;
import mille_bornes.modele.cartes.Botte;
import mille_bornes.vue.MilleBornes;

public class BotteVue extends CarteVue {

    private boolean grisee = false;

    public BotteVue(Botte botte, MilleBornes milleBornes, boolean grisee) {
        super(botte, milleBornes, false);
        setGrisee(grisee);
    }

    public void setGrisee(boolean grisee) {
        this.grisee = grisee;
        if(this.grisee) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setBrightness(-.5);
            setEffect(adjust);
        }
    }
}
