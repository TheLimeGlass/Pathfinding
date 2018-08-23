package me.limeglass.pathfinding.api.protocol;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public interface IPathfinding {

	//Pathfinding Goal - Avoid
	//(avoid|run away from) %*entitydatas%[, radius %-number%[, speed %-number%[, speed (if|when) (close|near) %-number%]]]
	
	/**
	 * Add a pathfinding goal to avoid another entity.
	 * @param priority The priority of the goal, max is 9.
	 * @param entity The entity that is avoiding.
	 * @param avoid The entity types that are to be avoided.
	 */
	public void addAvoidPathgoal(int priority, LivingEntity entity, EntityType... avoid);
	
	/**
	 * Add a pathfinding goal to avoid another entity.
	 * @param priority The priority of the goal, max is 9.
	 * @param entity The entity that is avoiding.
	 * @param radius The maximum radius in which to avoid from.
	 * @param avoid The entity types that are to be avoided.
	 */
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, EntityType... avoid);
	
	/**
	 * Add a pathfinding goal to avoid another entity.
	 * @param priority The priority of the goal, max is 9.
	 * @param entity The entity that is avoiding.
	 * @param radius The maximum radius in which to avoid from.
	 * @param speed The speed at which to avoid with.
	 * @param avoid The entity types that are to be avoided.
	 */
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, double speed, EntityType... avoid);
	
	/**
	 * Add a pathfinding goal to avoid another entity.
	 * @param priority The priority of the goal, max is 9.
	 * @param entity The entity that is avoiding.
	 * @param radius The maximum radius in which to avoid from.
	 * @param speed The speed at which to avoid with.
	 * @param speed The speed at which is increased if the avoider is close.
	 * @param avoid The entity types that are to be avoided.
	 */
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, double speed, double close, EntityType... avoid);
	
}
