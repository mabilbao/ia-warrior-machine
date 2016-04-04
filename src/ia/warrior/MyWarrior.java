package ia.warrior;
import ia.battle.camp.BattleField;
import ia.battle.camp.FieldCell;
import ia.battle.camp.FieldCellType;
import ia.battle.camp.Warrior;
import ia.battle.camp.WarriorData;
import ia.battle.camp.actions.Action;
import ia.battle.camp.actions.Attack;
import ia.battle.camp.actions.Skip;
import ia.battle.camp.actions.Suicide;
import ia.exceptions.RuleException;
import ia.warrior.actions.AFieldCell;
import ia.warrior.actions.WarriorMove;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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
		
		if ( this.countTurn == 1 ){
			System.out.println("--------------------------------------------------");
			System.out.println("Yo soy: " + this.getName()  + ". La posicion del enemigo: " + warriorData.getName() + " es X: " + enemyPosition.getX() + " Y: " + enemyPosition.getY() + ". Esta en nuestro rango? " + warriorData.getInRange());

		
		}else if ( this.countTurn == 2 ){
			System.out.println("Segundo turno, no hago una goma");
		
		
		
			action = skipWarrior();
		
		
		}else{
			System.out.println("Tercer turno, no hago una goma otra ve");
			
			
			
			
			action = skipWarrior();
			this.countTurn = 0;
			System.out.println("--------------------------------------------------");
		}
		
		
		if ( warriorData.getInRange() ){
			action = atackWarrior(enemyPosition);
			//action = new Suicide();
		}else{
			//action = new WarriorMove(this.getPosition(), enemyPosition);
			action = new WarriorMove(this.getPosition().getX(), this.getPosition().getY(), enemyPosition.getX(), enemyPosition.getY());
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
	
	@Override
	public void wasAttacked(int damage, FieldCell source) {
		// TODO Auto-generated method stub
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

