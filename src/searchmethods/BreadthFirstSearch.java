package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;
import utils.NodeLinkedList;

import java.util.List;

public class BreadthFirstSearch extends GraphSearch<NodeLinkedList> {

    public BreadthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    /**
     * In Breadth First Search, we can return the solution when we generate
     * a goal state (we don't need to add it to the frontier)
     * In this (optimized) version we are assuming that the initial state is never a goal state.
     * If this could happen, we should have an initial condition to verify that.
     */

    @Override
    public void addSuccessorToFrontier(State successor, Node parent) {
        if (!(frontier.containsState(successor) || explored.contains(successor))) {
            frontier.addLast(new Node(successor, parent));
        }
    }

    @Override
    public String toString() {
        return "Breadth first search";
    }
}