package me.limeglass.pathfinding.utils;

import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.localization.Language;
import ch.njol.skript.util.EnumUtils;
import me.limeglass.pathfinding.Pathfinding;
import me.limeglass.pathfinding.lang.PathfindingParser;

public class LangEnumParser<T extends Enum<T>> extends PathfindingParser<T> {
	
	private EnumUtils<T> enumUtil;
	private final Class<T> clazz;
	private final String codeName;

	public LangEnumParser(String variableNamePattern, Class<T> clazz) {
		super(variableNamePattern);
		this.clazz = clazz;
		this.codeName = variableNamePattern;
		try {
			this.enumUtil = new EnumUtils<T>(clazz, variableNamePattern + "s");
			ArrayList<String> enumNames = new ArrayList<String>();
			for (final T e : clazz.getEnumConstants()) {
				enumNames.addAll(Arrays.asList(Language.get_(variableNamePattern + "s" + "." + e.name())));
			}
			Pathfinding.getInstance().getConfiguration("syntax").set("Syntax.Enums." + clazz.getSimpleName() + ".names", enumNames);
		} catch (NullPointerException error) {
			Pathfinding.consoleMessage("&cThe class: " + clazz.getName() +  " for classinfo name: " + variableNamePattern + " isn't an Enum!");
		}
		Pathfinding.save("syntax");
	}

	@Nullable
	public T parse(String string, ParseContext parseContent) {
		if (string.startsWith(codeName + ":")) {
			string = string.substring(codeName.length() + 1, string.length());
		}
		string = string.replaceAll("_", " ");
		T result = enumUtil != null ? enumUtil.parse(string) : null; //Checks if the english.lang file contains the enum
		if (result != null) return result;
		string = string.replaceAll(" ", "_");
		try {
			return Enum.valueOf(clazz, string.toUpperCase()); //If it failed finding that enum in the english.lang or there wasn't one setup, do this fallback.
		} catch (IllegalArgumentException error) {
			return null;
		}
	}
	
	@Override
	public String toString(T t, int i) {
		return t.name().toLowerCase().replaceAll("_", " ");
	}

	@Override
	public String toVariableNameString(T t) {
		return codeName + ':' + t.toString();
	}

	@Override
	public String getVariableNamePattern() {
		return codeName + ":.+";
	}
}