package me.limeglass.pathfinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.util.SimpleEvent;
import me.limeglass.pathfinding.utils.Utils;
import me.limeglass.pathfinding.utils.annotations.AllChangers;
import me.limeglass.pathfinding.utils.annotations.Changers;
import me.limeglass.pathfinding.utils.annotations.Disabled;

public class Syntax {

	private static HashMap<String, String[]> modified = new HashMap<String, String[]>();
	private static HashMap<String, String[]> completeSyntax = new HashMap<String, String[]>();

	public static String[] register(Class<?> syntaxClass, String... syntax) {
		if (syntaxClass.isAnnotationPresent(Disabled.class)) return null;
		String type = "Expressions";
		if (Condition.class.isAssignableFrom(syntaxClass)) type = "Conditions";
		else if (Effect.class.isAssignableFrom(syntaxClass)) type = "Effects";
		else if (SimpleEvent.class.isAssignableFrom(syntaxClass)) type = "Events";
		else if (PropertyExpression.class.isAssignableFrom(syntaxClass)) type = "PropertyExpressions";
		String node = "Syntax." + type + "." + syntaxClass.getSimpleName() + ".";
		if (!Pathfinding.getInstance().getConfiguration("syntax").isSet(node + "enabled")) {
			Pathfinding.getInstance().getConfiguration("syntax").set(node + "enabled", true);
			Pathfinding.save("syntax");
		}
		if (syntaxClass.isAnnotationPresent(Changers.class) || syntaxClass.isAnnotationPresent(AllChangers.class)) {
			if (syntaxClass.isAnnotationPresent(AllChangers.class)) Pathfinding.getInstance().getConfiguration("syntax").set(node + "changers", "All changers");
			else {
				ChangeMode[] changers = syntaxClass.getAnnotation(Changers.class).value();
				Pathfinding.getInstance().getConfiguration("syntax").set(node + "changers", Arrays.toString(changers));
			}
			Pathfinding.save("syntax");
		}
		if (syntaxClass.isAnnotationPresent(Description.class)) {
			String[] descriptions = syntaxClass.getAnnotation(Description.class).value();
			Pathfinding.getInstance().getConfiguration("syntax").set(node + "description", descriptions[0]);
			Pathfinding.save("syntax");
		}
		if (!Pathfinding.getInstance().getConfiguration("syntax").getBoolean(node + "enabled")) {
			if (Pathfinding.getInstance().getConfig().getBoolean("NotRegisteredSyntax", false)) Pathfinding.consoleMessage(node.toString() + " didn't register!");
			return null;
		}
		if (!Pathfinding.getInstance().getConfiguration("syntax").isSet(node + "syntax")) {
			Pathfinding.getInstance().getConfiguration("syntax").set(node + "syntax", syntax);
			Pathfinding.save("syntax");
			return add(syntaxClass.getSimpleName(), syntax);
		}
		List<String> data = Pathfinding.getInstance().getConfiguration("syntax").getStringList(node + "syntax");
		if (!Utils.compareArrays(data.toArray(new String[data.size()]), syntax)) modified.put(syntaxClass.getSimpleName(), syntax);
		if (Pathfinding.getInstance().getConfiguration("syntax").isList(node + "syntax")) {
			List<String> syntaxes = Pathfinding.getInstance().getConfiguration("syntax").getStringList(node + "syntax");
			return add(syntaxClass.getSimpleName(), syntaxes.toArray(new String[syntaxes.size()]));
		}
		return add(syntaxClass.getSimpleName(), new String[]{Pathfinding.getInstance().getConfiguration("syntax").getString(node + "syntax")});
	}
	
	public static Boolean isModified(@SuppressWarnings("rawtypes") Class syntaxClass) {
		return modified.containsKey(syntaxClass.getSimpleName());
	}
	
	public static String[] get(String syntaxClass) {
		return completeSyntax.get(syntaxClass);
	}
	
	private static String[] add(String syntaxClass, String... syntax) {
		if (!completeSyntax.containsValue(syntax)) {
			completeSyntax.put(syntaxClass, syntax);
		}
		return syntax;
	}

}