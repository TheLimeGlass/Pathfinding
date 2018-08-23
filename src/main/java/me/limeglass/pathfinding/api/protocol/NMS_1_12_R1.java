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
import com.google.common.base.Preconditions;

import me.limeglass.pathfinding.utils.ReflectionUtil;

public class NMS_1_12_R1 implements IPathfinding {

	private static Class<?> craftLivingEntity, entityInsentient;
	private static ConstructorAccessor avoid;
	private static String version;

	static {
		version = ReflectionUtil.getVersion();
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
		Preconditions.checkArgument(avoid == null, "Could not find the PathfinderGoalAvoidTarget constructor for version " + version);
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
		for (Object goal : goals) method.invoke(goalSelector, priority, goal);
	}

//add pathfind[er] goal [[with] priority %-integer%] (0¦(avoid|run away from) %*entitydatas%[, radius %-number%[, speed %-number%[, speed (if|when) (close|near) %-number%]]]|1¦break door[s]|2¦breed[,[move[ment]] speed %-number%]|3¦eat grass|4¦(flee from the sun|seek shad(e|ow))[, [move[ment]] speed %-number%]|5¦(float (in[side]|on) water|swim)|6¦follow (owner|tamer)[, speed %-number%[, min[imum] distance %-number%[, max[imum] distance %-number%]]]|7¦follow (adult|parent)[s][, [move[ment]] speed %-number%]|8¦(fight back|react to|target) (damager|attacker) [[of] type] %*entitydatas%[, call ([for] help|reinforcement) %-boolean%]|9¦o(c|z)elot jump on blocks[, [move[ment]] speed %-number%]|10¦leap at target[, [leap] height %-number%]|11¦look at %*entitydatas%[, (radius|max[imum] distance) %-number%]|12¦melee attack %*entitydatas%[, [move[ment]] speed %-number%[, (memorize|do('nt| not) forget) target [for [a] long[er] time] %-boolean%]]|13¦move to[wards] target[, [move[ment]] speed %-number%[, (radius|max[imum] distance) %-number%]]|14¦target nearest [entity [of] type] %*entitydatas%[, check sight %-boolean%]|15¦o(c|z)elot attack|16¦open door[s]|17¦(panic|flee)[, [move[ment]] speed %-number%]|18¦look around randomly|19¦(walk around randomly|wander)[, [move[ment]] speed %-number%[, min[imum] [of] %-timespan% between mov(e[ment][s]|ing)]]|20¦sit|21¦[creeper] (explode|inflate|swell)|22¦squid (swim around|wander)|23¦shoot fireball[s]|24¦[silverfish] hide (in[side]|on) block[s]|25¦((call|summon|wake) [other] [hidden] silverfish[es])|26¦[enderman] pick[[ ]up] block[s]|27¦[enderman] place block[s]|28¦[enderman] attack player (staring|looking) at [their] eye[s]]|29¦ghast move to[wards] target|30¦ghast (idle move[ment]|wander|random fl(ight|y[ing]))|31¦(tempt to|follow players (holding|with)) %-itemstack%[, [move[ment]] speed %number%[, scared of player movement %-boolean%]]|32¦target [random] %*entitydatas% (if|when) (not |un)tamed|33¦guardian attack [entities]|34¦[z[ombie[ ]]pig[man]] attack [player[s]] (if|when) angry|35¦[z[ombie[ ]]pig[man]] (react to|fight back|target) (attacker|damager) (if|when) angry|36¦[rabbit] eat carrot crops|37¦[killer] rabbit [melee] attack|38¦slime [random] jump|39¦slime change (direction|facing) randomly|40¦slime (idle move[ment]|wander)|41¦follow %*entitydatas%[, radius %-number%[, speed %-number%[, [custom[ ]]name[d] %-string%]]]|42¦bow shoot[, [move[ment]] speed %-number%[, unk[nown] param[eter] %-number%[, follow range %-number%]]])) to %livingentities%
//add pathfinding goal %pathgoal% [[with] priority %-number%] to %entities%
	
}
