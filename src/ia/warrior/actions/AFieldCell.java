package ia.warrior.actions;
import ia.battle.core.FieldCell;

public class AFieldCell {

	/** costs to move sideways from one square to another. */
    protected static final int BASICMOVEMENTCOST = 10;
    /** costs to move diagonally from one square to another. */
    protected static final int DIAGONALMOVEMENTCOST = 15;

    private FieldCell fieldCell;
    
    /** the previous AbstractNode of this one on the currently calculated path. */
    private AFieldCell previous;

    /** weather or not the move from previous to this AbstractNode is diagonally. */
    private boolean diagonally;

    /** calculated costs from start AbstractNode to this AbstractNode. */
    private int gCosts;

    /** estimated costs to get from this AbstractNode to end AbstractNode. */
    private int hCosts;

    
    AFieldCell(FieldCell fieldCell){
    	this.setFieldCell(fieldCell);
    	this.setgCosts(0);
    	this.sethCosts(0);
    }
    
    public FieldCell getFieldCell() {
		return fieldCell;
	}

	public void setFieldCell(FieldCell fieldCell) {
		this.fieldCell = fieldCell;
	}

	/**
     * returns weather or not the move from the <code>previousAbstractNode</code> was
     * diagonally. If it is not diagonal, it is sideways.
     *
     * @return
     */
    public boolean isDiagonaly() {
        return diagonally;
    }

    /**
     * sets weather or not the move from the <code>previousAbstractNode</code> was
     * diagonally. If it is not diagonal, it is sideways.
     *
     * @param isDiagonaly
     */
    public void setIsDiagonaly(boolean isDiagonaly) {
        this.diagonally = isDiagonaly;
    }

    /**
     * returns the node set as previous node on the current path.
     *
     * @return the previous
     */
    public AFieldCell getPrevious() {
        return previous;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(AFieldCell previous) {
        this.previous = previous;
    }

    /**
     * returns <code>gCosts</code> + <code>hCosts</code>.
     * <p>
     *
     *
     * @return the fCosts
     */
    public int getfCosts() {
        return gCosts + hCosts;
    }

    /**
     * returns the calculated costs from start AbstractNode to this AbstractNode.
     *
     * @return the gCosts
     */
    public int getgCosts() {
        return gCosts;
    }

    /**
     * sets gCosts to <code>gCosts</code> plus <code>movementPanelty</code>
     * for this AbstractNode.
     *
     * @param gCosts the gCosts to set
     */
    private void setgCosts(int gCosts) {
        this.gCosts = gCosts;
    }

    /**
     * sets gCosts to <code>gCosts</code> plus <code>movementPanelty</code>
     * for this AbstractNode given the previous AbstractNode as well as the basic cost
     * from it to this AbstractNode.
     *
     * @param previousAbstractNode
     * @param basicCost
     */
    public void setgCosts(AFieldCell previousAbstractNode, int basicCost) {
        setgCosts(previousAbstractNode.getgCosts() + basicCost);
    }

    /**
     * sets gCosts to <code>gCosts</code> plus <code>movementPanelty</code>
     * for this AbstractNode given the previous AbstractNode.
     * <p>
     * It will assume <code>BASICMOVEMENTCOST</code> as the cost from
     * <code>previousAbstractNode</code> to itself if the movement is not diagonally,
     * otherwise it will assume <code>DIAGONALMOVEMENTCOST</code>.
     * Weather or not it is diagonally is set in the Map class method which
     * finds the adjacent AbstractNodes.
     *
     * @param previousAbstractNode
     */
    public void setgCosts(AFieldCell previousAbstractNode) {
        if (diagonally) {
            setgCosts(previousAbstractNode, DIAGONALMOVEMENTCOST);
        } else {
            setgCosts(previousAbstractNode, BASICMOVEMENTCOST);
        }
    }

    /**
     * calculates - but does not set - g costs.
     * <p>
     * It will assume <code>BASICMOVEMENTCOST</code> as the cost from
     * <code>previousAbstractNode</code> to itself if the movement is not diagonally,
     * otherwise it will assume <code>DIAGONALMOVEMENTCOST</code>.
     * Weather or not it is diagonally is set in the Map class method which
     * finds the adjacent AbstractNodes.
     *
     * @param previousAbstractNode
     * @return gCosts
     */
    public int calculategCosts(AFieldCell previousAbstractNode) {
        if (diagonally) {
            return (previousAbstractNode.getgCosts() + DIAGONALMOVEMENTCOST );
        } else {
            return (previousAbstractNode.getgCosts() + BASICMOVEMENTCOST );
        }
    }

    /**
     * calculates - but does not set - g costs, adding a movementPanelty.
     *
     * @param previousAbstractNode
     * @param movementCost costs from previous AbstractNode to this AbstractNode.
     * @return gCosts
     */
    public int calculategCosts(AFieldCell previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getgCosts() + movementCost );
    }

    /**
     * returns estimated costs to get from this AbstractNode to end AbstractNode.
     *
     * @return the hCosts
     */
    public int gethCosts() {
        return hCosts;
    }

    /**
     * sets hCosts.
     *
     * @param hCosts the hCosts to set
     */
    protected void sethCosts(int hCosts) {
        this.hCosts = hCosts;
    }

    /**
     * calculates hCosts for this AbstractNode to a given end AbstractNode.
     * Uses Manhatten method.
     *
     * @param endAbstractNode
     */
    public  void sethCosts(AFieldCell endNode){
        this.sethCosts((absolute(this.getFieldCell().getX() - endNode.getFieldCell().getX())
                + absolute(this.getFieldCell().getY() - endNode.getFieldCell().getY()))
                * BASICMOVEMENTCOST);
    }

    private int absolute(int a) {
        return a > 0 ? a : -a;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * returns a String containing the coordinates, as well as h, f and g
     * costs.
     *
     * @return
     */
    public String toString() {
        return "(" + this.getFieldCell().getX() + ", " + this.getFieldCell().getY() + "): h: "
                + gethCosts() + " g: " + getgCosts() + " f: " + getfCosts();
    }

    /**
     * returns weather the coordinates of AbstractNodes are equal.
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AFieldCell other = (AFieldCell) obj;
        if (this.getFieldCell().getX() != other.getFieldCell().getX()) {
            return false;
        }
        if (this.getFieldCell().getY() != other.getFieldCell().getY()) {
            return false;
        }
        return true;
    }

    /**
     * returns hash code calculated with coordinates.
     *
     * @return
     */
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.getFieldCell().getX();
        hash = 17 * hash + this.getFieldCell().getY();
        return hash;
    }

}
