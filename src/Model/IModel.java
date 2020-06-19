package Model;
//ckage ATP-Project-PartB;
import java.util.Observer;
import algorithms.mazeGenerators.*;
import algorithms.search.*;


public interface IModel {
    public void generateMaze(int row, int col);
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
    public void assignObserver(Observer o);
    public void solveMaze(Maze maze);
    public Solution getSolution();
}
