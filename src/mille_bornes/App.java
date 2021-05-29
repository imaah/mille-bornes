package mille_bornes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mille_bornes.vue.MilleBornes;

public class App extends Application {

    private static final int APP_WIDTH = 1200;
    private static final int APP_HEIGHT = 1000;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MilleBornes app = new MilleBornes(APP_WIDTH, APP_HEIGHT);

        Scene scene = new Scene(app.getHolder());
        stage.setScene(scene);
        stage.setTitle("1000 bornes");
        stage.setResizable(false);
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);
        stage.show();
    }
}
