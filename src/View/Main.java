package View;

import Model.MyModel;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, width/2, height/2);

        MyViewController view = fxmlLoader.getController();

        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();


        launch(args);
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

}