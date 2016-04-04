package ia.warrior.actions;

import ia.battle.camp.BattleField;
import ia.battle.camp.FieldCell;
import ia.battle.camp.FieldCellType;
import ia.battle.camp.actions.Action;
import ia.battle.camp.actions.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WarriorMove extends Move{

	private FieldCell nextMove;
	
	public WarriorMove (int oldX, int oldY, int newX, int newY) {
		nextMove = this.moveWarrior(oldX, oldY, newX, newY);
	}
	
	public WarriorMove (FieldCell myPosition, FieldCell enemyPosition) {
		nextMove = this.moveWarrior(myPosition, enemyPosition);
	}
	
	@Override
	public ArrayList<FieldCell> move() {
		ArrayList<FieldCell> cells = new ArrayList<FieldCell>();
		cells.add(nextMove);
		return cells;
	}
	
	/**
	 * 
	 * Get the next movement. This is a rustic way because it does not dodge walls.  
	 * 
	 * @param enemyPosition
	 * @return
	 */
	private FieldCell moveWarrior(FieldCell myPosition, FieldCell enemyPosition) {
		List<FieldCell> adj = BattleField.getInstance().getAdjacentCells(myPosition);
		FieldCell nextMove = myPosition;
		int closerDistance = Integer.MAX_VALUE;
		int distance;
		
		for(FieldCell cell : adj) {
			distance = computeDistance(cell, enemyPosition);
			if ((closerDistance > distance) && (cell.getFieldCellType() != FieldCellType.BLOCKED) &&
					!cell.equals(myPosition)) {
				nextMove = cell;
				closerDistance = distance;
			}
		}
		
		return nextMove;
	}

	private int computeDistance(FieldCell source, FieldCell target) {
		int distance = 0;

		distance = Math.abs(target.getX() - source.getX());
		distance += Math.abs(target.getY() - source.getY());

		return distance;
	}
	
	/**
	 * 
	 * Get the next movement from A*.
	 * Base model: http://software-talk.org/blog/2012/01/a-star-java/
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 * @return
	 */
	private FieldCell moveWarrior(int oldX, int oldY, int newX, int newY){

		List<AFieldCell> openList = new LinkedList<AFieldCell>();
		List<AFieldCell> closedList = new LinkedList<AFieldCell>();
		boolean done = false;

		AFieldCell actualPosition = new AFieldCell(BattleField.getInstance().getFieldCell(oldX, oldY)); 

		// add starting node to open list
		openList.add(actualPosition);
		done = false;
		AFieldCell current;
		
		while ( !done ) {
			// get node with lowest fCosts from openList
			current = lowestFInOpen(openList);
			//System.out.println(current);
			// add current node to closed list
			closedList.add(current);
			// delete current node from open list
			openList.remove(current);

			 // found goal
			if ((current.getFieldCell().getX() == newX) && (current.getFieldCell().getY() == newY)) {
				return calcPath( actualPosition, current ).get(0).getFieldCell();
			}

			// for all adjacent nodes:
			List<AFieldCell> adjacentNodes = getAdjacent(current, closedList);
			//System.out.println(adjacentNodes.size());
			
			for (int i = 0; i < adjacentNodes.size(); i++) {
				AFieldCell currentAdj = adjacentNodes.get(i);
				// if node is not in openList
				if (!containFieldCell(openList, currentAdj)) {
					// set current node as previous for this node
					currentAdj.setPrevious(current);
					// set h costs of this node (estimated costs to goal)
					currentAdj.sethCosts(new AFieldCell(BattleField.getInstance().getFieldCell(newX, newY)));
					// set g costs of this node (costs from start to this node)
					currentAdj.setgCosts(current);
					// add node to openList
					openList.add(currentAdj);
				} else {
					// costs from current node are cheaper than previous costs
					if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) {
						 // set current node as previous for this node
						currentAdj.setPrevious(current);
						// set g costs of this node (costs from start to this node)
						currentAdj.setgCosts(current);
					}
				}
			}
			//System.out.println(openList.size());
		}
		// unreachable
		return null;
	}
	
    /**
     * returns the node with the lowest fCosts.
     * @param <T>
     *
     * @return
     */
    private AFieldCell lowestFInOpen( List<AFieldCell> openList ) {
        // TODO currently, this is done by going through the whole openList!
    	AFieldCell cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getfCosts() < cheapest.getfCosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }
    
    /**
     * returns a LinkedList with nodes adjacent to the given node.
     * if those exist, are walkable and are not already in the closedList!
     */
    private List<AFieldCell> getAdjacent(AFieldCell node, List<AFieldCell> closedList ) {
    	
        int x = node.getFieldCell().getX();
        int y = node.getFieldCell().getY();
        List<AFieldCell> adj = new LinkedList<AFieldCell>();
        
        for (FieldCell fc : BattleField.getInstance().getAdjacentCells(node.getFieldCell())) {
			
        	AFieldCell temp = new AFieldCell(fc);
        	
        	if ( containFieldCell(closedList, temp) ) continue;
        	
        	if ( BattleField.getInstance().getFieldCell(temp.getFieldCell().getX(), temp.getFieldCell().getY()).getFieldCellType() == FieldCellType.BLOCKED){
        		continue;
        	}
        	
        	if ( temp.getFieldCell().getX() != x && temp.getFieldCell().getY() != y ){
        		temp.setIsDiagonaly(true);
        	}else{
        		temp.setIsDiagonaly(false);
        	}
        	adj.add(temp);
        }
        return adj;
    }

	
    /**
     * calculates the found path between two points according to
     * their given <code>previousNode</code> field.
     *
     * @param start
     * @param goal
     * @return
     */
    private List<AFieldCell> calcPath(AFieldCell start, AFieldCell goal) {
     // TODO if invalid nodes are given (eg cannot find from
     // goal to start, this method will result in an infinite loop!)
        LinkedList<AFieldCell> path = new LinkedList<AFieldCell>();

        AFieldCell curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = (AFieldCell) curr.getPrevious();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }
    
    private boolean containFieldCell(List<AFieldCell> container, AFieldCell n){
    	for (AFieldCell abstractNode : container) {
			if ( abstractNode.getFieldCell().getX() == n.getFieldCell().getX()
				&& abstractNode.getFieldCell().getY() == n.getFieldCell().getY() ){
				return true;
			}
		}
    	return false;
    }	
}
