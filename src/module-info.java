module milles.bornes {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports mille_bornes.controleur;
    exports mille_bornes.vue;
    exports mille_bornes;

    opens mille_bornes.vue;
    opens mille_bornes.modele;
    opens mille_bornes.vue.joueur;
    opens mille_bornes.vue.jeu;
}