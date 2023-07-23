package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BoulderGoal extends Goal {
	public boolean achieved(Game game) {
		return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
	}

	public String toString(Game game) {
		if (achieved(game))
			return "";
		else
			return ":boulders";
	}
}
