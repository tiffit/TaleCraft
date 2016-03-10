package de.longor.talecraft.script.wrappers.scoreboard;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardObjectWrapper implements IObjectWrapper {
	private Scoreboard scoreboard;
	
	public ScoreboardObjectWrapper(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
		
	}
	
	public Scoreboard internal() {
		return scoreboard;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public void addPlayerToTeam(String entityName, String teamName) {
		scoreboard.addPlayerToTeam(entityName, teamName);
	}
	
	public ScoreObjectWrapper getScore(String entityName, String objectiveName) {
		ScoreObjective objective = scoreboard.getObjective(objectiveName);
		return new ScoreObjectWrapper(scoreboard.getValueFromObjective(entityName, objective));
	}
	
	public ScoreObjectiveObjectWrapper getObjective(String objectiveName) {
		return new ScoreObjectiveObjectWrapper(scoreboard.getObjective(objectiveName));
	}
	
	// TODO: Add a way to create new objectives.
	// TODO: Add a way to remove objectives.
	
}
