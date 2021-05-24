module milles.bornes {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports mille_bornes.app;
    exports mille_bornes.app.mvc.controleur;
    opens mille_bornes.app.mvc.vue;
}