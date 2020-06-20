package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution sol;
    private int row_player;
    private int col_player;

    //StringProperty backgroundImage = new SimpleStringProperty();
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageGoalState = new SimpleStringProperty();

    public String getImageGoalState() {
        return imageGoalState.get();
    }

    public void setImageGoalState(String imageGoalState) {
        this.imageGoalState.set(imageGoalState);
    }




    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }






    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;

        draw();

    }




    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        //this.sol = sol;
        row_player= maze.getStartPosition().getRowIndex();
        col_player = maze.getStartPosition().getColumnIndex();

        draw();
    }

    public void draw()
    {

        if( maze!=null)//create maze
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.getRows();
            int col = maze.getCols();
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.BLACK);
            double w, h;
            //Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (maze.getMazeCell(i, j) == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null) {
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        } else {
                            graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                        }
                    }

                }
            }

            double h_goal = maze.getGoalPosition().getRowIndex() * cellHeight;
            double w_goal = maze.getGoalPosition().getColumnIndex() * cellWidth;
            Image goalImg = null;
            try {
                goalImg = new Image(new FileInputStream(getImageGoalState()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image goal....");
            }
            graphicsContext.drawImage(goalImg, w_goal, h_goal, cellWidth, cellHeight);


            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);

        }
    }

    public void drawSol(Solution sol){
        draw();
        this.sol = sol;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int row = maze.getRows();
        int col = maze.getCols();
        double cellHeight = canvasHeight / row;
        double cellWidth = canvasWidth / col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        //graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        graphicsContext.setFill(Color.GREEN);
        double w, h;
        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no file....");
        }
        for (int i = 1; i < sol.getSolutionPath().size() - 1; i++) {
            MazeState cell = (MazeState) sol.getSolutionPath().get(i);
            h = cell.getPos().getRowIndex() * cellHeight;
            w = cell.getPos().getColumnIndex() * cellWidth;
            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
        }
        //conunt++;
        double h_player = getRow_player() * cellHeight;
        double w_player = getCol_player() * cellWidth;
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image player....");
        }
        graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);
        sol = null;



    }

}
