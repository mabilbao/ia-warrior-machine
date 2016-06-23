package ia.warrior;
import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.Skip;
import ia.battle.core.actions.Suicide;
import ia.exceptions.RuleException;
import ia.warrior.actions.WarriorMove;

import java.util.ArrayList;
import java.util.Random;


public class MyWarrior extends Warrior {

	public MyWarrior(String name, int health, int defense, int strength,
			int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	private int countTurn = 1;
	private boolean wasAttackedByHunter = false;
	private int lastDamage;
	private int initX = -1;
	private int initY = -1;
	
	private Random random = new Random();
	
	@Override
	public Action playTurn(long tick, int actionNumber) {
		
		if (initX == -1 && initY == -1){
			initX = this.getPosition().getX();
			initY = this.getPosition().getY();
		}
		
		Action action = null;
		BattleField battlefield = BattleField.getInstance();
		WarriorData warriorData = battlefield.getEnemyData();
		FieldCell enemyPosition = warriorData.getFieldCell();
		
		WarriorData hunter = battlefield.getHunterData();
		
		if ( isPositionInRange(hunter.getFieldCell(), this.getPosition(), 5)) {
			//**System.out.println("Hunter - SI");
		}else{
			//**System.out.println("Hunter - NO");
		}
		
		
		if ( isPositionInRange(this.getPosition(), warriorData.getFieldCell(), this.getRange())) {
			//**System.out.println("En RANGO PARA ATACAR");
			if ( wasAttackedByHunter ){
				//**System.out.println("Fui atacado por el hunter... HUYO O ME PEGAN DE A DOS");
				action = getEscape(battlefield, hunter.getFieldCell());
				wasAttackedByHunter = false;
			}else{
//				if ( this.getHealth() < lastDamage && (getAttack() * (4 - countTurn) ) < warriorData.getHealth() ){
//				if ( this.getHealth() < lastDamage && ((this.getStrength() / 3) * (4 - countTurn) ) < warriorData.getHealth() ){
//					//**System.out.println("------------------------------");
//					//**System.out.println("ME SUICIDO");
//					//**System.out.println("------------------------------");
//					action = suicideWarrior();
//				}else{
					//**System.out.println("ATACOOOOO");
					action = atackWarrior(enemyPosition);
//				}
			}
		}else{
			action = getNextSpecialItem(battlefield);
			if ( action == null ) {
				//**System.out.println("NO Encuentro caja... Buscando al Rival");
				action = getWarriorWay(battlefield, enemyPosition);
				
				if ( action == null ) {
					
					action = getEscape(battlefield, enemyPosition);
					
					if ( action == null ) {
						
						if ( wasAttackedByHunter ){
							//**System.out.println("Fui atacado por el hunter... huyo de el");
							action = getEscape(battlefield, hunter.getFieldCell());
							wasAttackedByHunter = false;
						}
						
						if ( action == null ){
							//**System.out.println("NO Encuentro CAMINO NI ESCAPATORIA... ESPERO");
							action = skipWarrior();
						}
					}
					
					//**System.out.println("ME ESCAPO");
				}
				
			}else{
				//**System.out.println("Buscando Cajas...");
			}
		}
		
		
		this.countTurn++;
		return action;
	}
	
	private Action atackWarrior(FieldCell enemyPosition) {
		return new Attack(enemyPosition);
	}
	
	private Action skipWarrior(){
		return new Skip();
	}
	
	private Action suicideWarrior(){
		return new Suicide();
	}
	
	
	private int getAttack(){
	
		BattleField battlefield = BattleField.getInstance();
		WarriorData warriorData = battlefield.getEnemyData();
		FieldCell enemyPosition = warriorData.getFieldCell();
	
		float damage = this.getStrength();

		float distance =  battlefield.calculateDistance(this.getPosition(), enemyPosition);
		
		float range = this.getRange();
		
		float distanceFactor = 1 - (distance - 1) / range;
		
		damage *= distanceFactor;
		
		float defense = this.getDefense();
		defense = (float) (defense * (1 - Math.abs(random.nextGaussian())));

		damage -= defense;

		return (int) damage;
	}
	
	
	
	private WarriorMove getNextSpecialItem( BattleField battlefield ){
		
		int difference = 999;
		boolean isSureWay = true;
		WarriorMove nextSpecialItem = null;
		ArrayList<FieldCell> specialItems = battlefield.getSpecialItems();
		
		WarriorData hunter = battlefield.getHunterData();
		
		if (specialItems.size() != 0){
			for (FieldCell specialItem : specialItems) {
				if (
					(this.getPosition().getX() == specialItem.getX() && this.getPosition().getY() == specialItem.getY()) ||
					(0 == specialItem.getX() && 0 == specialItem.getY())
					){
					continue;
				}
				WarriorMove movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), specialItem.getX(), specialItem.getY());
				
				if ( movements.getNextMove().size() < difference ) {
					for (FieldCell fieldCell : movements.getNextMove()) {
						if (isPositionInRange(hunter.getFieldCell(), fieldCell, 5)){
							isSureWay = false;
						}
					}
					
					if ( isSureWay ){
						nextSpecialItem = movements;
						difference = movements.getNextMove().size();
					}
				}
				
				isSureWay = true;
			}	
		}
		
		return nextSpecialItem;
		
	}
	
	private WarriorMove getWarriorWay(BattleField battlefield, FieldCell enemyPosition){
		
		boolean isSureWay = true;
		WarriorMove movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), enemyPosition.getX(), enemyPosition.getY());
		WarriorData hunter = battlefield.getHunterData();
		
		for (FieldCell fieldCell : movements.getNextMove()) {
			if (isPositionInRange(hunter.getFieldCell(), fieldCell, 5)){
				isSureWay = false;
			}
		}
		
		if ( isSureWay ){
			return movements;
		}else{
			return null;
		}
	}
	
	private WarriorMove getEscape(BattleField battlefield, FieldCell enemyPosition){
		
		boolean isSureWay = true;
		WarriorMove movements = null;
		WarriorData hunter = battlefield.getHunterData();
		
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		int mapHeight = configurationManager.getMapHeight();
		int mapWidth = configurationManager.getMapWidth();
		
		int x,y;
		
		
		if ( enemyPosition.getX() <= (mapWidth/2) ) {
			// ir a la derecha
			if ( enemyPosition.getY() <= (mapHeight/2) ) {
				// ir ABAJO
				x = mapWidth-1;
				y = mapHeight-1;
			}else{
				// ir ARRIBA
				x = mapWidth-1;
				y = 0;
			}
		}else{
			// ir a la izquierda
			if ( enemyPosition.getY() <= (mapHeight/2) ) {
				// ir ABAJO
				x = 0;
				y = mapHeight-1;
			}else{
				// ir ARRIBA
				x = 0;
				y = 0;
			}
		}
		
		if ( (this.getPosition().getX() == x && this.getPosition().getY() == y) 
				|| battlefield.getFieldCell(x, y).getFieldCellType() == FieldCellType.BLOCKED){
			
			if ( (this.getPosition().getX() == initX && this.getPosition().getY() == initY) 
					|| battlefield.getFieldCell(initX, initY).getFieldCellType() == FieldCellType.BLOCKED){
				
				movements = null;
			}else{
				movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), initX, initY);
			}
		}else{
			movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), x, y);
		}
		
		return movements;
	}	
	
	private boolean isPositionInRange(FieldCell from, FieldCell to, int range) {
		int centerX = from.getX();
		int centerY = from.getY();

		int x = to.getX();
		int y = to.getY();

		if ((Math.pow(centerX - x, 2)) + (Math.pow(centerY - y, 2)) <= Math.pow(range, 2)) {
			return true;
		}

		return false;
	}
	
	@Override
	public void wasAttacked(int damage, FieldCell source) {
		BattleField battlefield = BattleField.getInstance();
		WarriorData warriorData = battlefield.getEnemyData();
		
		FieldCell enemyPosition = warriorData.getFieldCell();
		WarriorData hunter = battlefield.getHunterData();
		
		
		if ( source.getX() == enemyPosition.getX() && source.getY() == enemyPosition.getY() ){
			//**System.out.println("FUI ATACADO - " + damage + " - Por mi Rival");
		}else if ( source.getX() == hunter.getFieldCell().getX() && source.getY() == hunter.getFieldCell().getY() ) {
			//**System.out.println("FUI ATACADO - " + damage + " - Por el Hunter");
			wasAttackedByHunter = true;	
		}else{
			//**System.out.println("FUI ATACADO - " + damage);
		}
	}

	@Override
	public void enemyKilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
	
}

