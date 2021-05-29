module milles.bornes {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports mille_bornes.controleur;
    opens mille_bornes.vue;
    opens mille_bornes.modele;
    opens mille_bornes.vue.joueur;
    exports mille_bornes;
    opens mille_bornes.vue.jeu;
}