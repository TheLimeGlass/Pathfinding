package me.limeglass.pathfinding.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import me.limeglass.pathfinding.api.goals.PathfindingGoal;
import me.limeglass.pathfinding.lang.PathfindingExpression;
import me.limeglass.pathfinding.utils.annotations.Patterns;
import me.limeglass.pathfinding.utils.annotations.RegisterType;

@Name("Pathfinding - goals of entity")
@Description("Returns the goals of the defined entities.")
@Patterns("path[ ]finding goal[s] (of|from) %livingentities%")
@RegisterType("pathgoal")
//TODO Check/Handle single
public class ExprPathfindingGoals extends PathfindingExpression<PathfindingGoal> {
	
	//(avoid|run away from) %*entitydatas%[, radius %-number%[, speed %-number%[, speed (if|when) (close|near) %-number%]]]
	@Override
	protected PathfindingGoal[] get(Event event) {
		return null;
	}
}
