package me.limeglass.pathfinding.api.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.ConstructorAccessor;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.google.common.base.Preconditions;

public class NMS_1_12_R1 implements IPathfinding {

	private static Class<?> craftLivingEntity, entityInsentient;
	private static ConstructorAccessor avoid;
	private static MinecraftVersion version;

	static {
		version = MinecraftVersion.getCurrentVersion();
		craftLivingEntity = MinecraftReflection.getCraftBukkitClass("CraftLivingEntity");
		entityInsentient = MinecraftReflection.getMinecraftClass("EntityInsentient");
		avoid = Accessors.getConstructorAccessorOrNull(
				MinecraftReflection.getMinecraftClass("PathfinderGoalAvoidTarget"),
				MinecraftReflection.getMinecraftClass("EntityCreature"),
				Class.class, float.class, double.class, double.class
		);
	}

	@Override
	public void addAvoidPathgoal(int priority, LivingEntity entity, EntityType... avoid) {
		addAvoidPathgoal(priority, entity, 6.0F, avoid);
	}

	@Override
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, EntityType... avoid) {
		addAvoidPathgoal(priority, entity, radius, 1.0D, avoid);
	}

	@Override
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, double speed, EntityType... avoid) {
		addAvoidPathgoal(priority, entity, radius, 1.0D, 1.2D, avoid);
	}

	@Override
	public void addAvoidPathgoal(int priority, LivingEntity entity, float radius, double speed, double close, EntityType... types) {
		if (priority < 0) priority = 0;
		if (priority > 9) priority = 9;
		Preconditions.checkArgument(avoid == null, "Could not find the PathfinderGoalAvoidTarget constructor for version " + version.getVersion());
		List<Object> goals = new ArrayList<Object>();
		Object entityObject = craftLivingEntity.cast(entity);
		Object nmsEntity = null;
		try {
			nmsEntity = entityInsentient.cast(entityObject.getClass().getMethod("getHandle").invoke(entityObject));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("Could not properly grab the instance of entity " + entity.toString());
			return;
		}
		if (nmsEntity == null) {
			Bukkit.getConsoleSender().sendMessage("Could not properly grab the instance of entity " + entity.toString());
			return;
		}
		for (EntityType type : types) {
			Class<? extends Entity> entityClass = type.getEntityClass();
			if (LivingEntity.class.isAssignableFrom(entityClass)) {
				String className = entityClass.getSimpleName();
				if (className.equals("HumanEntity")) className = "Human";
				else if (className.equals("EntityLiving")) className = "Living";
				Class<?> nmsClass = MinecraftReflection.getMinecraftClass("Entity" + className);
				goals.add(avoid.invoke(nmsEntity, nmsClass, radius, speed, close));
			}
		}
		if (goals.size() <= 0) return;
		Object goalSelector = Accessors.getFieldAccessor(nmsEntity.getClass(), "goalSelector", true).get(nmsEntity);
		MethodAccessor method = Accessors.getMethodAccessor(goalSelector.getClass(), "a", int.class, MinecraftReflection.getMinecraftClass("PathfinderGoal"));
		for (Object goal : goals) {
			method.invoke(goalSelector, priority, goal);
		}
	}

}
