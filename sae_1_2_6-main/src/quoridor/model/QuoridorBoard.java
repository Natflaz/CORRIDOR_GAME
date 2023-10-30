package quoridor.model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * the board is in charge of indicating the possible moves for a given tile, and calculating the validity and destinations of a possible move.
 */
public class QuoridorBoard extends GridElement {

    /**
     * instantiates a board for Quoridor. The board counts 17 rows and 17 columns always.
     * @param x the x position of the board in regard to its parent
     * @param y the y position of the board in regard to its parent
     */
    public QuoridorBoard(int x, int y, GameStageModel gameStageModel) {
        super("quoridorboard", x, y, 9, 9, gameStageModel);
        resetReachableCells(false);
    }

    /**
     * returns the destination coordinates of the pawn, based on its position and the direction of travel.
     * It is strongly reccomended to use {@link #noWallOrEdgeInDirection(Pawn, Direction)} or {@link #noWallOrEdgeInDirection(int, int, Direction)}
     * to check wether or not the move is possible before calculating it, as this may throw an exception otherwise.
     * @param pawn the pawn whose position will be read
     * @param direction the direction of travel
     * @return the coordinates of the destination, int[2]
     */
    public int[] pawnDestination(Pawn pawn, Direction direction) {
        var pawnCords = getElementCell(pawn);
        return pawnDestination(pawnCords[0],pawnCords[1],direction);
    }

    /**
     * returns the destination coordinates of a hypothetical pawn, based on its position and the direction of travel.
     * It is strongly reccomended to use {@link #noWallOrEdgeInDirection(Pawn, Direction)} or {@link #noWallOrEdgeInDirection(int, int, Direction)}
     * to check wether or not the move is possible before calculating it, as this may throw an exception otherwise.
     * @param pawnRow the row the hypothetical pawn is placed on
     * @param pawnColumn the column the hypothetical pawn is placed on
     * @param direction the direction of travel
     * @return the coordinates of the destination, int[2]
     */
    public int[] pawnDestination(int pawnRow, int pawnColumn, Direction direction) {
        return getCellInDirection(pawnRow,pawnColumn,direction,1);
    }

    /**
     * was previously named canMovePawn(), but it was renamed since its functionality changed.
     * @param pawn the pawn whose position will be evaluated
     * @param direction the direction of travel
     * @return true only if no wall or edge of board is present in that direction
     */
    public boolean noWallOrEdgeInDirection(Pawn pawn, Direction direction) {
        int[] pawnCords = getElementCell(pawn);
        return noWallOrEdgeInDirection(pawnCords[0],pawnCords[1],direction);
    }

    /**
     * was previously named canMovePawn(), but it was renamed since its functionality changed.
     * @param pawnRow the row on which the hypothetical pawn is located
     * @param pawnColumn the column on which the hypothetical pawn is located
     * @param direction the direction of travel
     * @return true only if no wall or edge of board is present in that direction
     */
    public boolean noWallOrEdgeInDirection(int pawnRow, int pawnColumn, Direction direction) {
        int[] destCords = getCellInDirection(pawnRow,pawnColumn,direction,1);

        int destRow = destCords[0];
        int destColumn = destCords[1];

        if (!positionIsWithinBoard(pawnRow, pawnColumn)) {
            return false;
        }
        else if (!positionIsWithinBoard(destRow, destColumn)) {
            return false;
        }
        if(barrierExistsInDirection(pawnRow,pawnColumn,direction) ) {
            return false;
        }
        return true;
    }

    /**
     * @param row the row of the barrier origin
     * @param column the row of the barrier origin
     * @param type of the barrier
     * @return whether it is possible to place a barrier there
     */
    public boolean canPlaceBarrier(int row, int column, BarrierType type) {
//        debug();
        int[][] barrierBits;
        if(type == BarrierType.VERTICAL)
            barrierBits= new int[][]{
                new int[]{row,column},
                getCellInDirection(row,column,Direction.SOUTH, 0),
                getCellInDirection(row,column,Direction.SOUTH, 1)
        };
        else
            barrierBits= new int[][]{
                new int[]{row,column},
                getCellInDirection(row,column,Direction.EAST, 0),
                getCellInDirection(row,column,Direction.EAST, 1)
            };


        for (int[] bit : barrierBits) {
            if (!positionIsWithinBoard(bit[0],bit[1])) {
                return false;
            }
            if (positionIsBlockedByBarrier(bit[0], bit[1], type)) {
                return false;
            }
            if (bit[0] == getNbRows()-1 || bit[1] == getNbCols()-1) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param pawn the pawn whose position will be tested
     * @param direction the direction of the edge to check for
     * @return whether the pawn is on the edge of the board in the specified direction.
     * This is useful for checking the win conditions.
     */
    public boolean isOnEdge(Pawn pawn, Direction direction)
    {
        int[] pawnPosition = getElementCell(pawn);
        return  isOnEdge(pawnPosition[0],pawnPosition[1],direction);
    }
    /**
     * @param row the row the hypothetical pawn is on
     * @param column the column the hypothetical pawn is on
     * @param direction the direction of the edge to check for
     * @return whether the hypothetical pawn is on the edge of the board in the specified direction.
     * This is useful for checking the win conditions.
     */
    public boolean isOnEdge(int row, int column, Direction direction)
    {
        switch (direction) {
            case NORTH:
                return row == 0;
            case SOUTH:
                return row == nbRows-1;
            case EAST:
                return column == nbCols-1;
            case WEST:
                return column == 0;
            default:
                throw new IllegalArgumentException(type + " is not a valid type");
        }
    }

    /** convenience method that puts a pawn using {@link #putElement(GameElement, int, int, boolean)} at the center of a side of the board
     * @param pawn the pawn that will be put in the board
     * @param direction the side of the board that the pawn will be put at
     */
    public void initializePawnPosition(Pawn pawn, Direction direction) {
        int row;
        int column;
        switch (direction) {
            case NORTH:
                row = 0;
                column = nbCols/2;
                break;
            case SOUTH:
                row = nbRows-1;
                column = nbCols/2;
                break;
            case EAST:
                row = nbRows/2;
                column = nbCols-1;
                break;
            case WEST:
                row = nbRows/2;
                column = 0;
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid type");
        }
        putElement(pawn,row,column);
    }


    /**
     * @param pawn the pawn whose position will be used
     * @param direction the direction in which to check for a pawn
     * @return whether there is a pawn in that direction in relation to the pawn that was entered in the method
     */
    public boolean pawnExistsInDirection(Pawn pawn, Direction direction) {
        int[] pawnCords = getElementCell(pawn);
        return pawnExistsInDirection(pawnCords[0],pawnCords[1],direction);
    }


    /**
     * @param row the initial row
     * @param column the initial column
     * @param direction the direction in which to check for a pawn
     * @return whether there is a pawn in that direction
     */
    public boolean pawnExistsInDirection(int row, int column, Direction direction) {

        int[] otherPawnCell = getCellInDirection(row,column,direction,1);

        if (!positionIsWithinBoard(otherPawnCell[0], otherPawnCell[1])) {
            return false;
        }

        return positionHasPawn(otherPawnCell[0],otherPawnCell[1]);
    }

    /**
     * Gives all position that are valid to move the
     * @param element the element whose valid move cells will be computed
     */
    public void setValidCells(GameElement element) {
        resetReachableCells(false);
        List<Point> valid = null;
        if (element instanceof Pawn) {
            valid = computeValidCellsPawn((Pawn) element); // get all cells that are valid for the pawn
        } else if (element instanceof Barrier) {
            valid = computeValidCellsBarrier(); // get all cells that are valid for the barrier
        }
        if (valid != null) {
            for (Point point : valid) {
                reachableCells[point.x][point.y] = true;
            }
            lookChanged = true;
        }
    }

    private boolean needToJump(Pawn p, Direction d) {
        int[] pos = getElementCell(p);
        int[] dest = getCellInDirection(pos[0], pos[1], d, 1);
        List<GameElement> e = getElements(dest[0], dest[1]);
        if (e != null) {
            for (GameElement element : e) {
                if (element instanceof Pawn) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Point> jumpTo(Pawn p, Direction d) {
        List<Point> points = new ArrayList<>();
        if (!needToJump(p, d)) {
            return points;
        }
        int[] pos = getElementCell(p);
        int[] dest = getCellInDirection(pos[0], pos[1], d, 1);
        int[] bDest = getCellInDirection(pos[0], pos[1], d, 2);
        GameElement e = getBarrierGood(dest[0], dest[1], d);
        if (!(e instanceof Barrier)) {
            points.add(new Point(bDest[0], bDest[1]));
        } else {
            if (d == Direction.NORTH || d == Direction.SOUTH) {
                if (noWallOrEdgeInDirection(dest[0], dest[1], Direction.EAST)) {
                    points.add(new Point(dest[0], dest[1] + 1));
                }
                if (noWallOrEdgeInDirection(dest[0], dest[1], Direction.WEST)) {
                    points.add(new Point(dest[0], dest[1] - 1));
                }
            } else {
                if (noWallOrEdgeInDirection(dest[0], dest[1], Direction.NORTH)) {
                    points.add(new Point(dest[0] - 1, dest[1]));
                }
                if (noWallOrEdgeInDirection(dest[0], dest[1], Direction.SOUTH)) {
                    points.add(new Point(dest[0] + 1, dest[1]));
                }
            }
        }
        return points;
    }
    /**
     * Get all cells that are valid for the pawn (all cells that the pawn can go)
     * @param p the pawn
     * @return a list of point where the pawn can go
     */
    public List<Point> computeValidCellsPawn(Pawn p) {
        boolean north;
        boolean south;
        boolean west;
        boolean east;

        List<Point> points = new ArrayList<>();

        if (p.getColor() != getModel().getIdPlayer()){
            return points;
        }

        int[] pos = getElementCell(p);

        north = noWallOrEdgeInDirection(pos[0], pos[1], Direction.NORTH);
        south = noWallOrEdgeInDirection(pos[0], pos[1], Direction.SOUTH);
        west = noWallOrEdgeInDirection(pos[0], pos[1], Direction.WEST);
        east = noWallOrEdgeInDirection(pos[0], pos[1], Direction.EAST);

        if (north) {
            if (needToJump(p, Direction.NORTH)) {
                if (pos[0] > 2) {
                    points.addAll(jumpTo(p, Direction.NORTH));
                }
            } else {
                points.add(new Point(pos[0]-1, pos[1]));
            }
        }
        if (south) {
            if (needToJump(p, Direction.SOUTH)) {
                if (pos[0] < getNbRows() - 2) {
                    points.addAll(jumpTo(p, Direction.SOUTH));
                }
            } else {
                points.add(new Point(pos[0]+1, pos[1]));
            }
        }
        if (west) {
            if (needToJump(p, Direction.WEST)) {
                if (pos[1] > 2) {
                    points.addAll(jumpTo(p, Direction.WEST));
                }
            } else {
                points.add(new Point(pos[0], pos[1]-1));
            }
        }
        if (east) {
            if (needToJump(p, Direction.EAST)) {
                if (getNbCols() > pos[1] + 2) {
                    points.addAll(jumpTo(p, Direction.EAST));
                }
            } else {
                points.add(new Point(pos[0], pos[1]+1));
            }
        }

        return points;
    }

    /**
     * Get all cells that are valid for the barrier (all cells where the barrier can be placed)
     * @return a list of point where the barrier can be placed
     */
    private List<Point> computeValidCellsBarrier() {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < nbRows-1; i++) {
            for (int j = 0; j < nbCols-1; j++) {
                if (canPlaceBarrier(i, j, BarrierType.HORIZONTAL)) {
                    points.add(new Point(i, j));
                }
                if (canPlaceBarrier(i, j, BarrierType.VERTICAL)) {
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }

    private boolean positionIsBlockedByBarrier(int row, int column, BarrierType type) {

        if(barrierOriginOnCell(row,column)) {
            return true;
        }

        if (inlineWallsBlock(row, column, type)) {
            return true;
        }

        if (blockedAtIntersection(row, column)) {
            return true;
        }

        else return false;
    }

    private boolean inlineWallsBlock(int row, int column, BarrierType type) {
        int[][] checkForWallOriginsThatBlock = new int[][]{
                getCellInDirection(row, column,Direction.NORTH,1),
                getCellInDirection(row, column,Direction.WEST,1),
        };
        BarrierType[] types = new BarrierType[]
        {
            BarrierType.VERTICAL,
            BarrierType.HORIZONTAL
        };

        int[] cords;
        BarrierType type2;
        for (int i = 0; i < checkForWallOriginsThatBlock.length; i++) {
            cords = checkForWallOriginsThatBlock[i];
            type2 = types[i];

            if(!positionIsWithinBoard(cords[0],cords[1]))
                continue;
            Barrier barrier = getBarrier(cords[0],cords[1]);
            if(barrier != null && ((barrier.getBarrierType() == type2 && type2 == BarrierType.HORIZONTAL) || (type == type2 && barrier.getBarrierType() == type2))) {
                return true;
            }
        }
        return false;
    }
    private boolean blockedAtIntersection(int row, int column) {
        int[] hBC = getCellInDirection(row, column,Direction.WEST,0);
        int[] vBC = getCellInDirection(row, column,Direction.NORTH,0);

        Barrier hBarrier;
        if (positionIsWithinBoard(hBC[0], hBC[1])) {
////            System.out.println(String.format("inboard : %b, row:%d ,col:%d",positionIsWithinBoard(hBC[0], hBC[1]),hBC[0], hBC[1]));
            hBarrier = getBarrier(hBC[0], hBC[1]);
        } else hBarrier = null;

        Barrier vBarrier;
        if (positionIsWithinBoard(vBC[0], vBC[1])) {
            vBarrier = getBarrier(vBC[0], vBC[1]);
        } else vBarrier = null;

        boolean hBarrierBlocks = hBarrier!=null && hBarrier.getBarrierType() == BarrierType.HORIZONTAL;
        boolean vBarrierBlocks = vBarrier!=null && vBarrier.getBarrierType() == BarrierType.VERTICAL;

        if(hBarrierBlocks || vBarrierBlocks)
            return true;
        return false;
    }
    private int[] getCellInDirection(int row, int column, Direction direction, int distance) {
        switch (direction)
        {
            case NORTH :
                return new int[]{row - distance,column};
            case SOUTH :
                return new int[]{row + distance,column};
            case EAST :
                return new int[]{row,column + distance};
            case WEST :
                return new int[]{row,column - distance};
            default:
                throw new IllegalArgumentException(direction+" is not a valid direction");
        }
    }
    private boolean positionIsWithinBoard(int row, int column) {
        return row >= 0 && row < getNbRows() && column >= 0 && column < getNbCols();
    }
    private boolean validHorizontalBarrierPosition(int row, int column) {
        return (row%2 == 1 && column%2 == 0) && positionIsWithinBoard(row,column);
    }
    private boolean validVerticalBarrierPosition(int row, int column) {
        return (row % 2 == 0 && column % 2 == 1) && positionIsWithinBoard(row,column);
    }
    private Barrier getBarrier(int row, int column) {
        GameElement element =  getElement(row, column);

        return element instanceof Barrier ? (Barrier) element : null;
    }
    private boolean barrierExistsInDirection(int row, int column, Direction direction) {
        return blockedByBarrier(row, column, direction);
    }

    private GameElement getBarrierGood(int row, int column, Direction direction) {
        int[][] dest = new int[2][2];
        BarrierType barrierType = BarrierType.HORIZONTAL;
        switch (direction) {
            case NORTH:
                if (row <= 0)
                    break;
                dest[0] = new int[2];
                dest[0][0] = row - 1;
                dest[0][1] = column;
                if (column > 0) {
                    dest[1] = new int[2];
                    dest[1][0] = row - 1;
                    dest[1][1] = column - 1;
                }
                break;
            case SOUTH:
                dest[0] = new int[2];
                dest[0][0] = row;
                dest[0][1] = column;
                if (column > 0) {
                    dest[1] = new int[2];
                    dest[1][0] = row;
                    dest[1][1] = column - 1;
                }
                break;
            case EAST:
                dest[0] = new int[2];
                dest[0][0] = row;
                dest[0][1] = column;
                if (row > 0) {
                    dest[1] = new int[2];
                    dest[1][0] = row - 1;
                    dest[1][1] = column;
                    barrierType = BarrierType.VERTICAL;
                }
                break;
            case WEST:
                if (column <= 0)
                    break;
                dest[0] = new int[2];
                dest[0][0] = row;
                dest[0][1] = column - 1;
                if (row > 0) {
                    dest[1] = new int[2];
                    dest[1][0] = row - 1;
                    dest[1][1] = column - 1;
                    barrierType = BarrierType.VERTICAL;
                }
                break;
        }

        List<GameElement> e1, e2;
        if (dest[0] != null) {
            e1 = getElements(dest[0][0], dest[0][1]);
            if (e1 != null) {
                for (GameElement e : e1) {
                    if (e instanceof Barrier) {
                        if (((Barrier) e).getBarrierType() == barrierType) {
                            return e;
                        }
                    }
                }
            }
        }
        if (dest[1] != null) {
            e2 = getElements(dest[1][0], dest[1][1]);
            if (e2 != null) {
                for (GameElement e : e2) {
                    if (e instanceof Barrier) {
                        if (((Barrier) e).getBarrierType() == barrierType) {
                            return e;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean blockedByBarrier(int row, int column, Direction direction) {
        if (getBarrierGood(row, column, direction) != null)
            return true;
        return false;
    }

    private boolean barrierOriginOnCell(int row, int column){

        GameElement element = getElement(row, column);
////        System.out.println("element : "+element);

        if (element instanceof Barrier)
            return true;
        return false;
    }
    private boolean positionHasPawn(int row, int column){
        GameElement pawn = getElement(row, column);
        return getElement(row, column) instanceof Pawn;
    }
}