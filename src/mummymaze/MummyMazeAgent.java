package mummymaze;

import agent.Agent;
import mummymaze.arrays.Trap;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MummyMazeAgent extends Agent<MummyMazeState>{

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environemt) {
        super(environemt);
        initialEnvironment = (MummyMazeState) environemt.clone();
        heuristics.add(new HeuristicTileDistance());
        heuristics.add(new HeuristicTilesOutOfPlace());
        heuristics.add(new HeuristicGoalDistance());
        heuristic = heuristics.get(0);
    }

    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();
        return environment;
    }

    //existe uma função q transforma uma string num array
    //igualar a matriz a cada array

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        LinkedList<Trap> traps= new LinkedList<>();

        char[][] matrix = new char [13][13];
        int KeyLine = 0;
        int KeyColumm = 0;

        for (int i = 0; i < 13; i++) {
            matrix[i]=scanner.nextLine().toCharArray();

            for (int j = 0; j < 13; j++) {
                switch (matrix[i][j]){
                    case 'A' :              // Trap
                         traps.add(new Trap(i,j)) ;

                        break;
                    case 'C' :              // Key
                        KeyLine = i;
                        KeyColumm = j;
                        break;
                }
            }
        }
        initialEnvironment = new MummyMazeState(matrix,KeyLine,KeyColumm,traps);
        resetEnvironment();
        return environment;
    }
}