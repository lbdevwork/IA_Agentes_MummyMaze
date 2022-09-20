package mummymaze;

import agent.Action;
import agent.Problem;

import java.util.LinkedList;
import java.util.List;

//TODO mudar o goal -> estar na porta pos porta = saida
//TODO no state mudar os can move  1º para o heroi 2º para o mummy
//as mumias movem se para o pé do heroi se naõ move se para cima


public class MummyMazeProblem extends Problem<MummyMazeState> {

    protected List<Action> actions;
    private MummyMazeState goalstate;


    public MummyMazeProblem(MummyMazeState initialState) {
        super(initialState);
        actions = new LinkedList<Action>() {{
            add(new ActionDown());
            add(new ActionUp());
            add(new ActionRight());
            add(new ActionLeft());
            add(new ActionStay());
        }};

    }

    @Override
    public List<Action<MummyMazeState>> getActions(MummyMazeState state) {
        List<Action<MummyMazeState>> possibleActions = new LinkedList<>();

        if (state.isDead()) {
            return possibleActions;
        }

        for (Action action : actions) {
            if (action.isValid(state)) {
                possibleActions.add(action);
            }
        }
        return possibleActions;
    }

    @Override
    public MummyMazeState getSuccessor(MummyMazeState state, Action action){
        MummyMazeState successor = state.clone();
        action.execute(successor);
        return successor;
    }

    @Override
    public boolean isGoal(MummyMazeState state) {

        int hLine = state.getHeroLine();
        int hColumn = state.getHeroColumn();
        int eLine = state.getGoalLine();
        int eColumn = state.getGoalColumm();

        System.out.println("Linha e Coluna do Herói: "+hLine+"/"+hColumn);
        System.out.println("Saída: "+eLine+"/"+eColumn);

        if(hColumn == eColumn && hLine == eLine){
            goalstate=state;
            return true;
        }

        return false;

    }
    /* Todo get line and collum and check if matches coordinantes of exit */

    public MummyMazeState getGoalstate(MummyMazeState state){
        if(goalstate!=null){
            return state;
        }
        return null;
    }

    @Override
    public double computePathCost(List<Action> path) {
        return path.size();
    }


}
