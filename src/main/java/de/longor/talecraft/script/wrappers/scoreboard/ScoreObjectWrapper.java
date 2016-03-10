package de.longor.talecraft.script.wrappers.scoreboard;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.Score;

public class ScoreObjectWrapper implements IObjectWrapper {
	private Score score;
	
	public ScoreObjectWrapper(Score score) {
		this.score = score;
	}
	
	public Score internal() {
		return score;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public String getHolderName() {
		return score.getPlayerName();
	}
	
	public int getScore() {
		return score.getScorePoints();
	}
	
	public void setScore(int value) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.setScorePoints(value);
	}
	
	public void increaseScore(int amount) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.increseScore(amount);
	}
	
	public void decreaseScore(int amount) {
		if(!score.getObjective().getCriteria().isReadOnly())
			score.decreaseScore(amount);
	}
}
