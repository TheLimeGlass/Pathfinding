package me.limeglass.pathfinding.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.njol.skript.classes.Changer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Changers {
    public Changer.ChangeMode[] value();
}