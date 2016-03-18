package de.longor.talecraft.script.wrappers.scoreboard;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;

public class ScoreboardCriteriaObjectWrapper implements IObjectWrapper {
	private IScoreCriteria criteria;

	public ScoreboardCriteriaObjectWrapper(IScoreCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public IScoreCriteria internal() {
		return criteria;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getName() {
		return criteria.getName();
	}

	public boolean isReadOnly() {
		return criteria.isReadOnly();
	}

	public EnumRenderType getRenderType() {
		return criteria.getRenderType();
	}

}
