package de.raffi.autocraft.converter;

public abstract class Converter<T> {

	public abstract String stringify(T t);
	public abstract T create(String s);
}
