package de.raffi.autocraft.converter;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConverterLocation extends Converter<Location>{

	@Override
	public String stringify(Location t)  {
		return t.getWorld().getName() + ";" + t.getX() + ";"+ t.getY() + ";"+ t.getZ() + ";"+ t.getYaw() + ";"+ t.getPitch();
	}

	@Override
	public Location create(String s){
		String[] l = s.split(";");
		return new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]),Double.valueOf(l[2]), Double.valueOf(l[3]), Float.valueOf(l[4]), Float.valueOf(l[5]));
	}

}
