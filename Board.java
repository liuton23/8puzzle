/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {

    private final int[][] board;
    private final int dim;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dim = tiles.length;
        board = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dim + "\n");
        for (int[] row : board) {
            for (int num : row) {
                str.append(String.format("%2d ", num));
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        int out = 0;
        for (int i = 1; i < dim*dim; i++) {
            int[] coord = indexToCoord(i);
            if (board[coord[0]][coord[1]] != i) out++;
        }
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != 0) {
                    int[] goal = indexToCoord(board[i][j]);
                    manhattan += Math.abs(i - goal[0]) + Math.abs(j - goal[1]);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return  Arrays.deepEquals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> nbs = new Queue<>();
        int row = 0;
        int col = 0;
        int[][] copy1 = copyBoard();
        int[][] copy2 = copyBoard();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if (row == dim - 1 || row == 0 || col == 0 || col == dim - 1) {
            if (row == 0) {
                copy2[row + 1][col] = 0;
                copy2[row][col] = board[row + 1][col];
            }
            if (row == dim - 1) {
                copy2[row - 1][col] = 0;
                copy2[row][col] = board[row - 1][col];
            }
            if (col == 0) {
                copy1[row][col + 1] = 0;
                copy1[row][col] = board[row][col + 1];
            }
            if (col == dim - 1) {
                copy1[row][col - 1] = 0;
                copy1[row][col] = board[row][col - 1];
            }
            if ((row == 0 || row == dim - 1) && (col == 0 || col == dim - 1)) {
                nbs.enqueue(new Board(copy1));
                nbs.enqueue(new Board(copy2));
            } else {
                int[][] copy3 = copyBoard();
                if (row == 0 || row == dim - 1) {
                    copy1[row][col - 1] = 0;
                    copy1[row][col] = board[row][col - 1];
                    copy3[row][col + 1] = 0;
                    copy3[row][col] = board[row][col + 1];
                } else {
                    copy2[row + 1][col] = 0;
                    copy2[row][col] = board[row + 1][col];
                    copy3[row - 1][col] = 0;
                    copy3[row][col] = board[row - 1][col];
                }
                nbs.enqueue(new Board(copy1));
                nbs.enqueue(new Board(copy2));
                nbs.enqueue(new Board(copy3));
            }
        } else {
            int[][] copy3 = copyBoard();
            int[][] copy4 = copyBoard();
            copy1[row][col - 1] = 0;
            copy1[row][col] = board[row][col - 1];
            copy2[row][col + 1] = 0;
            copy2[row][col] = board[row][col + 1];
            copy3[row + 1][col] = 0;
            copy3[row][col] = board[row + 1][col];
            copy4[row - 1][col] = 0;
            copy4[row][col] = board[row - 1][col];
            nbs.enqueue(new Board(copy1));
            nbs.enqueue(new Board(copy2));
            nbs.enqueue(new Board(copy3));
            nbs.enqueue(new Board(copy4));
        }
        return nbs;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = copyBoard();
        if (board[0][0] == 0) {
            twin[0][1] = board[1][0];
            twin[1][0] = board[0][1];
        } else if (board[0][1] == 0) {
            twin[0][0] = board[1][0];
            twin[1][0] = board[0][0];
        } else {
            twin[0][0] = board[0][1];
            twin[0][1] = board[0][0];
        }
        return new Board(twin);
    }


    private int[] indexToCoord(int index) {
        int[] coord = new int[2];
        if (index <= dim) {
            coord[1] = index - 1;
        } else if (index % dim == 0) {
            coord[0] = index / dim - 1;
            coord[1] = dim - 1;
        } else {
            coord[0] = (index - (index % dim)) / dim;
            coord[1] = index % dim - 1;
        }
        return coord;
    }

    private int[][] copyBoard() {
        int[][] copy = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] b = new int[3][3];
        b[0][0] = 1;
        b[0][2] = 2;
        b[1][0] = 1;
        b[1][2] = 8;
        b[2][0] = 4;
        b[2][1] = 6;
        b[2][2] = 7;
        b[1][1] = 5;
        Board bd = new Board(b);
        System.out.println(bd);
        Iterable<Board> nb = bd.neighbors();
        for (Board bo: nb) {
            System.out.println(bo);
        }
        System.out.println(bd.manhattan());
        System.out.println(bd.twin());
    }

}
