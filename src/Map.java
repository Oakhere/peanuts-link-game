import java.util.*;

import static java.lang.Math.*;

public class Map {
    Vertex[][] map;
    ArrayList<Vertex> path;
    public Map() {
        map = new Vertex[10][16];
        for (int i = 0; i < 160; i++) {
            map[i / 16][i % 16] = new Vertex(i / 16, i % 16, i / 10);
        }
        path = null;
    }

    public void shuffle(int times) {
        Random random = new Random();
        for (int i = 0; i < times; i++) {
            int firstRandom = random.nextInt(160);
            int secondRandom = random.nextInt(160);
            Vertex first = map[firstRandom / 16][firstRandom % 16];
            Vertex second = map[secondRandom / 16][secondRandom % 16];
            if (first.enabled && second.enabled) {
                int temp;
                temp = first.info;
                first.info = second.info;
                second.info = temp;
            }
        }
    }

    public boolean linkedWithNoCorner(Vertex v, Vertex w) {
        if (v.row != w.row && v.col != w.col) {
            return false;
        }
        if (v.row == w.row) {
            for (int i = min(v.col, w.col) + 1; i < max(v.col, w.col); i++) {
                if (map[v.row][i].enabled) {
                    return false;
                }
            }
            path = new ArrayList<>(Arrays.asList(v, w));
            return true;
        }
        for (int i = min(v.row, w.row) + 1; i < max(v.row, w.row); i++) {
            if (map[i][v.col].enabled) {
                return false;
            }
        }
        path =  new ArrayList<>(Arrays.asList(v, w)) ;
        return true;
    }

    public boolean linkedWithOneCorner(Vertex v, Vertex w) {
        if (v.row == w.row || v.col == w.col) {
            return false;
        }
        Vertex cornerOne = map[v.row][w.col];
        Vertex cornerTwo = map[w.row][v.col];
        if (linkedWithNoCorner(v, cornerOne) && linkedWithNoCorner(w, cornerOne) && !cornerOne.enabled) {
            path = new ArrayList<>(Arrays.asList(v, cornerOne, w));
            return true;
        }
        if (linkedWithNoCorner(v, cornerTwo) && linkedWithNoCorner(w, cornerTwo) && !cornerTwo.enabled) {
            path = new ArrayList<>(Arrays.asList(v, cornerTwo, w));
            return true;
        }
        clearPath();
        return false;
    }

    public boolean linkedWithTwoCorners(Vertex v, Vertex w) {
        for (int i = 0; i < 10; i++) {
            Vertex cornerOne = map[i][v.col];
            Vertex cornerTwo = map[i][w.col];
            if (linkedWithNoCorner(v, cornerOne) && linkedWithNoCorner(cornerOne, cornerTwo)
                    && linkedWithNoCorner(cornerTwo, w) && !cornerOne.enabled && !cornerTwo.enabled) {
                path = new ArrayList<>(Arrays.asList(v, cornerOne, cornerTwo, w));
                return true;
            }
        }
        for (int i = 0; i < 16; i++) {
            Vertex cornerOne = map[v.row][i];
            Vertex cornerTwo = map[w.row][i];
            if (linkedWithNoCorner(v, cornerOne) && linkedWithNoCorner(cornerOne, cornerTwo)
                    && linkedWithNoCorner(cornerTwo, w) && !cornerOne.enabled && !cornerTwo.enabled) {
                path = new ArrayList<>(Arrays.asList(v, cornerOne, cornerTwo, w));
                return true;
            }
        }
        clearPath();
        return false;
    }

    public boolean hasLegalPath(Vertex v, Vertex w) {
        return linkedWithNoCorner(v, w) || linkedWithOneCorner(v, w) || linkedWithTwoCorners(v, w);
    }

    public void clearPath() {
        path = null;
    }

    public Vertex[] hint() {
        for (Vertex[] row1 : map) {
            for (Vertex v : row1) {
                for (Vertex[] row2 : map) {
                    for (Vertex w : row2) {
                        if (!v.enabled || !w.enabled || v == w) {
                            continue;
                        }
                        if (v.info == w.info && hasLegalPath(v, w)) {
                            clearPath();
                            return new Vertex[]{v, w};
                        }
                    }
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Map map = new Map();
        Vertex v = map.map[0][0];
        Vertex w = map.map[1][1];
        Vertex k = map.map[0][1];
        k.enabled = false;
        System.out.println(map.hasLegalPath(v, k));
    }
}


