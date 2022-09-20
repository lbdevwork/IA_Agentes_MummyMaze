package searchmethods;

import agent.State;
import java.util.List;

public class GreedyBestFirstSearch extends InformedSearch {

    //f = h
    @Override
    public void addSuccessorToFrontier(State successor, Node parent) {

        double g = parent.getG() + successor.getAction().getCost();
        if (!frontier.containsState(successor)) {
            if (!explored.contains(successor)) {
                frontier.add(new Node(successor, parent, g, heuristic.compute(successor)));
            }
        } else if (frontier.getNode(successor).getG() > g) {
            frontier.removeNode(successor);
            frontier.add(new Node(successor, parent, g, heuristic.compute(successor)));
        }

    }

    @Override
    public String toString() {
        return "Greedy best first search";
    }
}