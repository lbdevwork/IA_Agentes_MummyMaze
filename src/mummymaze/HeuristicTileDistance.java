package mummymaze;

import agent.Heuristic;

public class HeuristicTileDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        problem.isGoal(state);
        return state.computeTileDistances(problem.getGoalstate(state));
    }

    @Override
    public String toString(){
        return "Tiles distance to final position";
    }
}
