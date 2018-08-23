package me.limeglass.pathfinding.api.goals;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.limeglass.pathfinding.Pathfinding;

public class AvoidGoal implements PathfindingGoal {

	private final LivingEntity entity;
	private double speed, close;
	private EntityType[] types;
	private int priority = 4;
	private float radius;
	
	AvoidGoal(LivingEntity entity, EntityType... types) {
		this(entity, 6.0F, types);
	}
	
	AvoidGoal(LivingEntity entity, float radius, EntityType... types) {
		this(entity, radius, 1.0D, types);
	}
	
	AvoidGoal(LivingEntity entity, float radius, double speed, EntityType... types) {
		this(entity, radius, speed, 1.2D, types);
	}
	
	AvoidGoal(LivingEntity entity, float radius, double speed, double close, EntityType... types) {
		this.radius = radius;
		this.entity = entity;
		this.speed = speed;
		this.types = types;
		this.close = close;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public EntityType[] getAvoidTypes() {
		return types;
	}
	
	public void setAvoidTypes(EntityType... types) {
		this.types = types;
	}
	
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @return The speed at which is increased to when close to the avoiding entities.
	 */
	public double getCloseSpeed() {
		return close;
	}

	public void setCloseSpeed(double close) {
		this.close = close;
	}
	
	public void execute() {
		Pathfinding.getPathfindingAPI().addAvoidPathgoal(priority, entity, radius, speed, close, types);
	}

}
