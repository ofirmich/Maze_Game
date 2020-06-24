package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private static MyViewModel viewmodel;
    private MyModel model;
    public static MyViewModel getInstance() {
        if(viewmodel == null){
            viewmodel = new MyViewModel();
        }
        return viewmodel;
    }
    private  MyViewModel() {model= MyModel.getInstance();}


    private Maze maze;
    private int rowChar;
    private int colChar;
    private Solution sol;


/*    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
        this.sol = null;
    }*/

    public Solution getSol() {
        return sol;
    }

    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = model.getMaze();
            }
            else {
                Maze maze = model.getMaze();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(model.getSolution() != null)//Solve Maze
                    {
                        this.sol = model.getSolution();

                    }
                        this.rowChar = rowChar;
                        this.colChar = colChar;


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    this.sol = null;////////////////////
                }
            }

            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;

        /*
            direction = 2 -> Down
            direction = 8 -> Up
            direction = 4 -> Left
            direction = 6 -> Right

            direction = 1 -> Down Left
            direction = 9 -> Up Right
            direction = 7 -> Up Left
            direction = 3 -> Down Right
         */

        switch (keyEvent.getCode()){
            case NUMPAD8:
                direction = 8;
                break;
            case NUMPAD2:
                direction = 2;
                break;
            case NUMPAD4:
                direction = 4;
                break;
            case NUMPAD6:
                direction = 6;
                break;

            case NUMPAD7:
                direction = 7;
                break;
            case NUMPAD1:
                direction = 1;
                break;
            case NUMPAD9:
                direction = 9;
                break;
            case NUMPAD3:
                direction = 3;
                break;
        }
        model.updateCharacterLocation(direction);
    }

    public void solveMaze(Maze maze)
    {
        model.solveMaze(maze);
      //  model.getSolution();
    }


    public void getSolution()
    {
        model.getSolution();
    }

    public void saveMaze(File file){model.saveMaze(file);}

    public void loadMaze(File file){model.loadMaze(file);}

}

