/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private final Stack<Board> solution = new Stack<>();
    private final int moves;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<ManhattanNode> solving = new MinPQ<ManhattanNode>();
        ManhattanNode ini = new ManhattanNode(initial, null);
        ManhattanNode iniTwin = new ManhattanNode(initial.twin(), null);
        solving.insert(ini);
        solving.insert(iniTwin);
        ManhattanNode goal = solving.delMin();
        while (!goal.getBoard().isGoal()) {
            for (Board n : goal.getBoard().neighbors()) {
                if (goal.getPre() == null ||
                        !n.equals(goal.getPre().getBoard())) {
                    solving.insert(new ManhattanNode(n, goal));
                }
            }
            goal = solving.delMin();
        }
        solution.push(goal.getBoard());
        moves = goal.getMove();
        while (goal.getPre() != null) {
            goal = goal.getPre();
            solution.push(goal.getBoard());
        }
        solvable = goal.getBoard().equals(initial);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solution;
        }
        return null;
    }


    private class ManhattanNode implements Comparable<ManhattanNode> {
        private final Board board;
        private final ManhattanNode pre;
        private int move;

        public ManhattanNode(Board board, ManhattanNode pre) {
            this.pre = pre;
            this.board = board;
            if (pre == null) {
                this.move = 0;
            }
            else {
                this.move = pre.move + 1;
            }
        }

        public int getManhattan() {
            return board.manhattan();
        }

        public int getMove() {
            return move;
        }

        public Board getBoard() {
            return board;
        }

        public ManhattanNode getPre() {
            return pre;
        }

        public int compareTo(ManhattanNode that) {
            return Integer.compare(board.manhattan() + move,
                                   that.getManhattan() + that.getMove());
        }
    }


    // test client (see below)
    public static void main(String[] args) {
        int[][] b = new int[3][3];
        b[0][0] = 5;
        b[0][1] = 2;
        b[0][2] = 1;
        b[1][0] = 4;
        b[1][1] = 8;
        b[1][2] = 3;
        b[2][0] = 7;
        b[2][1] = 6;
        b[2][2] = 0;
        // solve the slider puzzle
        // 5  2  1
        // 4  8  3
        // 7  6  0
        Board bd = new Board(b);
        Solver s = new Solver(bd);
        System.out.println(s.moves());
    }
}
