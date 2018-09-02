package me.limeglass.pathfinding.elements.expressions;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import me.limeglass.pathfinding.api.goals.AvoidGoal;
import me.limeglass.pathfinding.api.goals.PathfindingGoal;
import me.limeglass.pathfinding.lang.PathfindingExpression;
import me.limeglass.pathfinding.utils.annotations.Patterns;
import me.limeglass.pathfinding.utils.annotations.Single;

@Name("Pathfinding - Avoid goal")
@Description("Create and return a new avoid pathfinding goal.")
@Patterns("(avoid|run away from) %entitytypes% [[(with|,|in a)] radius [of] %-number%] [[(with|,|at)] speed %-number%] [[(with|,|at)] [(close|run away)] speed [of] %-number%]")
@Single
public class ExprAvoidGoal extends PathfindingExpression<PathfindingGoal> {
	
	@Override
	protected PathfindingGoal[] get(Event event) {
		if (isNull(event, 0)) return null;
		AvoidGoal goal = new AvoidGoal(expressions.getAll(event, EntityType.class));
		if (!isNull(event, 1)) {
			goal.setRadius(expressions.getSingle(event, Number.class, 0).floatValue());
		}
		if (!isNull(event, 2)) {
			goal.setSpeed(expressions.getSingle(event, Number.class, 0).doubleValue());
		}
		if (!isNull(event, 3)) {
			goal.setCloseSpeed(expressions.getSingle(event, Number.class, 0).floatValue());
		}
		return new PathfindingGoal[] {goal};
	}

}
