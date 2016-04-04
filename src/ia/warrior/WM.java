package ia.warrior;
import ia.battle.camp.Warrior;
import ia.battle.camp.WarriorManager;
import ia.exceptions.RuleException;


public class WM extends WarriorManager {

	@Override
	public String getName() {
		return "Equipo Marton";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
		
		//return new WarriorExample1("Marton 2", 30, 20, 20, 10, 20);
		return new MyWarrior("Marton 2", 30, 20, 20, 20, 10);
	}

}
