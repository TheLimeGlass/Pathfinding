package me.limeglass.pathfinding.elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import me.limeglass.pathfinding.Pathfinding;
import me.limeglass.pathfinding.utils.ReflectionUtil;

public class Events {
	
	public Events() {
		//registerEvent(EvtPlayerSwitchServer.class, "[player] [bungee[[ ]cord]] switch server[s]", "[bungee[[ ]cord]] player switching [of] server[s]");
	}
	
	@SuppressWarnings("unused")
	private void registerEvent(Class<? extends Event> event, String... patterns) {
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pathfinding.getInstance().getNameplate() + patterns[i];
		}
		Object[] values = new Object[] {true, patterns, getEventValues(event)};
		String[] nodes = new String[] {"enabled", "patterns", "eventvalues"};
		for (int i = 0; i < nodes.length; i++) {
			if (!Pathfinding.getInstance().getConfiguration("syntax").isSet("Syntax.Events." + event.getSimpleName() + "." + nodes[i])) {
				Pathfinding.getInstance().getConfiguration("syntax").set("Syntax.Events." + event.getSimpleName() + "." + nodes[i], values[i]);
			}
		}
		Pathfinding.save("syntax");
		if (Pathfinding.getInstance().getConfiguration("syntax").getBoolean("Syntax.Events." + event.getSimpleName() + ".enabled", true)) {
			//TODO find a way to make the stupid Spigot Yaml read properly for user editing of event patterns.
			Skript.registerEvent(event.getSimpleName(), SimpleEvent.class, event, patterns);
		}
	}
	
	@SafeVarargs
	private final List<String> getEventValues(Class<? extends Event>... events) {
		List<String> classes = new ArrayList<String>();
		try {
			Method method = EventValues.class.getDeclaredMethod("getEventValuesList", int.class);
			method.setAccessible(true);
			for (Class<? extends Event> event : events) {
				for (int i = -1; i < 2; i++) {
					List<?> eventValueInfos = (List<?>) method.invoke(EventValues.class, i);
					if (eventValueInfos != null) {
						for (Object eventValueInfo : eventValueInfos) {
							Class<?> e = ReflectionUtil.getField("event", eventValueInfo.getClass(), eventValueInfo);
							if (e != null && (e.isAssignableFrom(event) || event.isAssignableFrom(e))) {
								Class<?> clazz = ReflectionUtil.getField("c", eventValueInfo.getClass(), eventValueInfo);
								if (clazz != null) classes.add(clazz.getSimpleName());
							}
						}
					}
				}
			}
		} catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException error) {
			error.printStackTrace();
		}
		return classes;
	}
}