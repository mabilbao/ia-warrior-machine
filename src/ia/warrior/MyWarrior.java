package ia.warrior;
import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.Skip;
import ia.exceptions.RuleException;
import ia.warrior.actions.WarriorMove;


public class MyWarrior extends Warrior {

	public MyWarrior(String name, int health, int defense, int strength,
			int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	private int countTurn = 1;

	@Override
	public Action playTurn(long tick, int actionNumber) {
		
		Action action = null;
		BattleField battlefield = BattleField.getInstance();
		WarriorData warriorData = battlefield.getEnemyData();
		FieldCell enemyPosition = warriorData.getFieldCell();
		
		WarriorData hunter = battlefield.getHunterData();
		
		if ( isPositionInRange(hunter.getFieldCell(), this.getPosition(), 5)) {
			System.out.println("Hunter - SI");
		}else{
			System.out.println("Hunter - NO");
		}
		
		
		if ( isPositionInRange(this.getPosition(), warriorData.getFieldCell(), this.getRange())) {
			System.out.println("En RANGO --> ATACO");
			action = atackWarrior(enemyPosition);
		}else{
			action = getNextSpecialItem(battlefield);
			if ( action == null ) {
				System.out.println("NO Encuentro caja... Buscando al Rival");
				action = getWarriorWay(battlefield, enemyPosition);
				
				if ( action == null ) {
					
					action = getEscape(battlefield, enemyPosition);
					
					if ( action == null ) {
						System.out.println("NO Encuentro CAMINO NI ESCAPATORIA... ESPERO");
						action = skipWarrior();
					}
					
					System.out.println("ME ESCAPO");
				}
				
			}else{
				System.out.println("Buscando Cajas...");
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
	
	
	private WarriorMove getNextSpecialItem( BattleField battlefield ){
		
		int difference = 999;
		boolean isSureWay = true;
		WarriorMove nextSpecialItem = null;
		ArrayList<FieldCell> specialItems = battlefield.getSpecialItems();
		
		WarriorData hunter = battlefield.getHunterData();
		
		if (specialItems.size() != 0){
			for (FieldCell specialItem : specialItems) {
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
		
		if ( enemyPosition.getX() <= (mapWidth/2) ) {
			// ir a la derecha
			if ( enemyPosition.getY() <= (mapHeight/2) ) {
				// ir ABAJO
				if ( this.getPosition().getX() == mapWidth-1 && this.getPosition().getY() == 1 ){
					return null;
				}
				movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), mapWidth-1, 1);
				
			}else{
				// ir ARRIBA
				if ( this.getPosition().getX() == mapWidth-1 && this.getPosition().getY() == mapHeight-1 ){
					return null;
				}
				movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), mapWidth-1, mapHeight-1);
			}
			
		}else{
			// ir a la izquierda
			if ( enemyPosition.getY() <= (mapHeight/2) ) {
				// ir ABAJO
				if ( this.getPosition().getX() == 1 && this.getPosition().getY() == 1 ){
					return null;
				}
				movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), 1, 1);
			}else{
				// ir ARRIBA
				if ( this.getPosition().getX() == 1 && this.getPosition().getY() == mapHeight-1 ){
					return null;
				}
				movements = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), 1, mapHeight-1);
			}
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
		System.out.println("FUI ATACADO - " + damage);
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

