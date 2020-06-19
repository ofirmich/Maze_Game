package View;

import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Main extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = new FXMLLoader(getClass().getResource("View/MazeWindow.fxml"))
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 900));
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource(("MyView.fxml")));
        Parent root = loader.load();

        this.primaryStage = primaryStage;

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = loader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        primaryStage.setTitle("oshi");
        primaryStage.setScene(new Scene(root,700  ,500));
        primaryStage.show();




      //  solveButton.setDisable(false);
    }

    public static void setScene(Scene newScene){
        //Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        //primaryStage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
