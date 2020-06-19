package Model;

import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyModel extends Observable implements IModel{

    private Maze maze;
    private int rowChar;
    private int colChar;
    private Solution sol;


    public MyModel() {
        maze = null;
        rowChar= 2;
        colChar = 2;
        sol = null;

    }

    public void updateCharacterLocation(int direction)
    {
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

        switch(direction)
        {
            case 8: //Up
                if(rowChar!=0 && maze.getMazeCell(rowChar-1 , colChar) != 1) {
                    rowChar--;
                }
                break;

            case 2: //Down
                  if(rowChar!=maze.getRows()-1 && maze.getMazeCell(rowChar+1 , colChar) != 1)  {
                      rowChar++;
                  }
                break;

            case 4: //Left
                  if(colChar!=0 && maze.getMazeCell(rowChar , colChar-1) != 1){
                colChar--;}
                break;

            case 6: //Right
                  if(colChar!=maze.getCols()-1 && maze.getMazeCell(rowChar , colChar+1) != 1){
                colChar++;}
                break;


            case 1: //Down Left
                if(rowChar!=maze.getRows()-1 && colChar!=0 &&  maze.getMazeCell(rowChar+1 , colChar-1) != 1 &&(maze.getMazeCell(rowChar , colChar-1) != 1||maze.getMazeCell(rowChar+1 , colChar) != 1)){
                colChar--;
                rowChar++;}
                break;

            case 9: //Up Right
                if(rowChar!=0 && colChar!=maze.getCols()-1 &&  maze.getMazeCell(rowChar-1 , colChar+1) != 1 &&(maze.getMazeCell(rowChar , colChar+1) != 1||maze.getMazeCell(rowChar-1 , colChar) != 1)){
                colChar++;
                rowChar--;}
                break;

            case 7: //Up Left
                if(rowChar!=0 && colChar!=0 &&  maze.getMazeCell(rowChar-1 , colChar-1) != 1&&(maze.getMazeCell(rowChar-1 , colChar) != 1 || maze.getMazeCell(rowChar , colChar-1) != 1)){
                colChar--;
                rowChar--;}
                break;

            case 3: //Down Right
                if(rowChar!=maze.getRows()-1 && colChar!=maze.getCols()-1 && maze.getMazeCell(rowChar+1 , colChar+1) != 1 &&(maze.getMazeCell(rowChar+1 , colChar) != 1 || maze.getMazeCell(rowChar , colChar+1) != 1)){
                colChar++;
                rowChar++;}
                break;

        }

        setChanged();
        notifyObservers();
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(Maze maze) {
        ISearchable mazeToSolve = new SearchableMaze(maze);
        ASearchingAlgorithm searcher = new BestFirstSearch();
        sol =searcher.solve(mazeToSolve);
      /*  rowChar= this.rowChar;
        colChar= this.colChar;*/
        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return this.sol;
       /* ISearchable mazeToSolve = new SearchableMaze(maze);
        ASearchingAlgorithm searcher = new BestFirstSearch();
        Solution sol =searcher.solve(mazeToSolve);
        setChanged();
        notifyObservers();
        return sol;*/

    }


    public void generateMaze(int row, int col) {
        AMazeGenerator maze = new MyMazeGenerator();
        Maze mazeGen = maze.generate(row, col);
        this.maze = mazeGen;
        rowChar = this.maze.getStartPosition().getRowIndex();
        colChar = this.maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers();
    }

    @Override
    public Maze getMaze() {
        return maze;
    }


}
