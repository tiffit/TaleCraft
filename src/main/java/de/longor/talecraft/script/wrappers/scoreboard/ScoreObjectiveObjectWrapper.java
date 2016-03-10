package de.longor.talecraft.script.wrappers.scoreboard;

import java.util.List;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria.EnumRenderType;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;

public class ScoreObjectiveObjectWrapper implements IObjectWrapper {
	private ScoreObjective objective;
	
	public ScoreObjectiveObjectWrapper(ScoreObjective objective) {
		this.objective = objective;
	}
	
	@Override
	public ScoreObjective internal() {
		return objective;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public String getName() {
		return objective.getName();
	}
	
	public String getDisplayName() {
		return objective.getName();
	}
	
	public EnumRenderType getRenderType() {
		return objective.getRenderType();
	}
	
	public ScoreObjectWrapper getValue(String name) {
		return new ScoreObjectWrapper(objective.getScoreboard().getValueFromObjective(name, objective));
	}
	
	public ScoreboardCriteriaObjectWrapper getCriteria() {
		return new ScoreboardCriteriaObjectWrapper(objective.getCriteria());
	}
	
	public ScoreboardObjectWrapper getScoreboard() {
		return new ScoreboardObjectWrapper(objective.getScoreboard());
	}
	
}
