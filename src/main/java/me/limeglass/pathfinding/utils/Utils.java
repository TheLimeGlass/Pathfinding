package me.limeglass.pathfinding.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Arrays;
import org.bukkit.ChatColor;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Timespan;
import me.limeglass.pathfinding.Pathfinding;

public class Utils {
	
	public static boolean compareArrays(String[] arg1, String[] arg2) {
		if (arg1.length != arg2.length) {
			return false;
		}
		Arrays.sort(arg1);
		Arrays.sort(arg2);
		return Arrays.equals(arg1, arg2);
	}
	
	public static Boolean isEnum(Class<?> clazz, String object) {
		try {
			final Method method = clazz.getMethod("valueOf", String.class);
			method.setAccessible(true);
			method.invoke(clazz, object.replace("\"", "").trim().replace(" ", "_").toUpperCase());
			return true;
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getEnum(Class<T> clazz, String object) {
		try {
			final Method method = clazz.getMethod("valueOf", String.class);
			method.setAccessible(true);
			return (T) method.invoke(clazz, object.replace("\"", "").trim().replace(" ", "_").toUpperCase());
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			Pathfinding.consoleMessage("&cUnknown type " + object + " in " + clazz.getName());
			return null;
		}
	}
	
	public static Class<?> getArrayClass(Class<?> parameter){
		return Array.newInstance(parameter, 0).getClass();
	}

	public static String cc(String text) {
		return ChatColor.translateAlternateColorCodes((char)'&', text);
	}
	
	@SuppressWarnings("deprecation")
	public static int getTicks(Timespan time) {
		if (Skript.methodExists(Timespan.class, "getTicks_i")) {
			Number tick = time.getTicks_i();
			return tick.intValue();
		} else {
			return time.getTicks();
		}
	}

	public static void copyDirectory(File source, File destination) throws IOException {
		if (source.isDirectory()) {
			if (!destination.exists()) destination.mkdir();
			String files[] = source.list();
			for(int i = 0; i < files.length; i++) {
				copyDirectory(new File(source, files[i]), new File(destination, files[i]));
			}
		} else if (source.exists()) {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(destination);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}
	
	public static int findPort(int start, int max) {
		int port = start;
		Throwable lastException = null;
		while (port < max) {
			ServerSocket socket = null;
			try {
				socket = new ServerSocket(port);
				socket.setReuseAddress(true);
				return port;
			} catch (IOException e) {
				lastException = e;
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {}
				}
			}
			port++;
		}
		if (lastException != null) Skript.exception(lastException, "Couldn't find a port between " + start + " and " + port);
		return -1;
	}

}