package me.limeglass.pathfinding.api.goals;

import org.bukkit.entity.LivingEntity;

public interface PathfindingGoal {

	/**
	 * @return The entity involved in the goal.
	 */
	public LivingEntity getEntity();

	/**
	 * @return The priority of the goal on the entity.
	 */
	public int getPriority();
	
	/**
	 * @param priority The priority index of position.
	 */
	public void setPriority(int priority);
	
	/**
	 * Adds the goal to the entity.
	 */
	public void execute();

}
