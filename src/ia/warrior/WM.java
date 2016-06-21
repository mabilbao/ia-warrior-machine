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
		
//		if ( specialItems.size() > 10 ){
			return new MyWarrior("Marton", 17, 10, 20, 43, 10);
//		}else{
//			return new MyWarrior("Marton", 17, 20, 40, 13, 10);
//		}
	}

}
