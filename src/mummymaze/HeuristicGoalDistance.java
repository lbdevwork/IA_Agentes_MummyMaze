package mummymaze;

import agent.Heuristic;

public class HeuristicGoalDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        problem.isGoal(state);
        return state.computeGoalDistance(state);
    }

    @Override
    public String toString(){
        return "Tiles distance to final position";
    }
}
