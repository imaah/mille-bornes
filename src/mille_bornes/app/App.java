package mille_bornes.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mille_bornes.app.mvc.vue.MilleBornes;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MilleBornes app = new MilleBornes();

        Scene scene = new Scene(app.getHolder());
        stage.setScene(scene);
        stage.setTitle("1000 bornes");
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}
