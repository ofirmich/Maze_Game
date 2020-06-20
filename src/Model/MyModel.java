package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import Client.IClientStrategy;
import Server.ServerStrategySolveSearchProblem;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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
    public void solveMaze(Maze mazeToSolve) {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();

                        toServer.writeObject(mazeToSolve); //send maze to server
                        toServer.flush();
                        sol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
     //   solveSearchProblemServer.stop();
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


    public void generateMaze(int rowM, int colM) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rowM, colM};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(mazeDimensions[0]*mazeDimensions[1])+13 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        rowChar = maze.getStartPosition().getRowIndex();
                        colChar = maze.getStartPosition().getColumnIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
       // mazeGeneratingServer.stop();
        setChanged();
        notifyObservers();
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    public void saveMaze(File file) {
        try{
        FileOutputStream out = new FileOutputStream(file);
        ObjectOutputStream obj = new ObjectOutputStream(out);
        obj.writeObject(this.getMaze());
        obj.flush();
        obj.close();}
        catch (IOException e){}

    }

    @Override
    public void loadMaze(File file) {
        try{
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream obj = new ObjectInputStream(in);
            this.maze = (Maze)obj.readObject();
            this.rowChar = maze.getStartPosition().getRowIndex();
            this.colChar = maze.getStartPosition().getColumnIndex();
            obj.close();
        }
        catch (IOException | ClassNotFoundException e){}
        setChanged();
        notifyObservers();
    }
}
