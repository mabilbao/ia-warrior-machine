package ia.warrior;
import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;


public class WM extends WarriorManager {

	@Override
	public String getName() {
		return "Marton";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
//		name, health, defense, strength, speed, range
		
		ArrayList<FieldCell> specialItems = BattleField.getInstance().getSpecialItems();
		
		if ( specialItems.size() >= 10){
			return new MyWarrior("mv", 20, 7, 20, 43, 10);
	
		// Si la densidad de campos es alta, no es necesario que vaya a buscar al rival, pero si debo huir
		}else if ( specialItems.size() < 10 && specialItems.size() >= 5 ){
			return new MyWarrior("mm", 28, 7, 30, 25, 10);
		}else{
			return new MyWarrior("mf", 35, 5, 35, 15, 10);
		}
	}

}
