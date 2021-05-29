package mille_bornes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mille_bornes.vue.MilleBornes;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MilleBornes app = new MilleBornes(1200, 1000);

        Scene scene = new Scene(app.getHolder());
        stage.setScene(scene);
        stage.setTitle("1000 bornes");
        stage.setResizable(false);
        stage.setWidth(1200);
        stage.setHeight(1000);
        stage.show();
    }
}
