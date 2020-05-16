package org.emmazarate;

public class Simulation {

    int width;
    int height;
    int[][] board;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
    }

    public void printBoard() {
        System.out.println("---");
        for (int y = 0; y < height; y++) {
            String line = "|";
            for (int x = 0; x < width; x++) {
                if (this.board[x][y] == 0) {
                    line += ".";
                } else {
                    line += "*";
                }
            }
            line += "|";
            System.out.println(line);
        }
        System.out.println("---\n");
    }

    public void setAlive(int x, int y) {
        this.setState(x, y, 1);
    }

    public void setDead(int x, int y) {
        this.setState(x, y, 0);
    }

    public void setState(int x, int y, int state) {
        if (x < 0 || x >= width) {
            return;
        }
        if (y < 0 || y >= height) {
            return;
        }

        this.board[x][y] = state;
    }

    public int countAliveNeighbors(int x, int y) {
        int count = 0;

        // top neighbors
        count += getState(x - 1,y - 1);
        count += getState(x,y - 1);
        count += getState(x + 1,y - 1);

        // next door neighbors
        count += getState(x - 1,y);
        count += getState(x + 1,y);

        // bottom neighbors
        count += getState(x - 1,y + 1);
        count += getState(x,y + 1);
        count += getState(x + 1,y + 1);

        return count;
    }

    public int getState(int x, int y) {
        if (x < 0 || x >= width) {
            return 0;
        }

        if (y < 0 || y >= height) {
            return 0;
        }

        return  this.board[x][y];
    }

    public void step() {
//        TODO: Refactor step() function to be more D.R.Y.
        int[][] newBoard = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aliveNeighbors = countAliveNeighbors(x, y);

                if(getState(x, y) == 1) {
                    if(aliveNeighbors < 2) {
                        newBoard[x][y] = 0;
                    } else if(aliveNeighbors == 2 || aliveNeighbors == 3){
                        newBoard[x][y] = 1;
                    } else if(aliveNeighbors > 3) {
                        newBoard[x][y] = 0;
                    }
                }else {
                    if(aliveNeighbors == 3) {
                        newBoard[x][y] = 1;
                    }
                }
            }
        }

        this.board = newBoard;
    }
    public static void main(String[] args) {
        Simulation simulation = new Simulation(8, 5);
        simulation.setAlive(2, 2);
        simulation.setAlive(3, 2);
        simulation.setAlive(4, 2);

        simulation.printBoard();
        simulation.step();
        simulation.printBoard();
        simulation.step();
        simulation.printBoard();
    }
}
