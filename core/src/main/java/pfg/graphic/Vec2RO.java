/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Vecteur en lecture seule
 * 
 * @author pf
 *
 */

public class Vec2RO implements Serializable, Position
{
	private static final long serialVersionUID = 1L;
	protected volatile double x;
	protected volatile double y;
	private static NumberFormat formatter = new DecimalFormat("#0.00");

	@SuppressWarnings("unused")
	public Vec2RO(double longueur, double angle, boolean useless)
	{
		x = Math.cos(angle) * longueur;
		y = Math.sin(angle) * longueur;
	}

	public Vec2RO(double requestedX, double requestedY)
	{
		x = requestedX;
		y = requestedY;
	}

	public final double dot(Vec2RO other)
	{
		return x * other.x + y * other.y;
	}

	public final Vec2RW plusNewVector(Vec2RO other)
	{
		return new Vec2RW(x + other.x, y + other.y);
	}

	public final Vec2RW minusNewVector(Vec2RO other)
	{
		return new Vec2RW(x - other.x, y - other.y);
	}

	@Override
	public final Vec2RW clone()
	{
		return new Vec2RW(this.x, this.y);
	}

	public final double squaredDistance(Vec2RO other)
	{
		double tmp_x = x - other.x, tmp_y = y - other.y;
		return tmp_x * tmp_x + tmp_y * tmp_y;
	}

	public final double distance(Vec2RO other)
	{
		return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
	}

	// Renvoie une approximation de la distance. Très rapide
	public final double distanceFast(Vec2RO other)
	{
		double dx = Math.abs(x - other.x);
		double dy = Math.abs(y - other.y);
		return Math.max(dx, dy) + 0.414 * Math.min(dx, dy);
	}

	@Override
	public final String toString()
	{
		return "(" + formatter.format(x) + "," + formatter.format(y) + ")";
	}

	public final boolean equals(Vec2RO other)
	{
		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode()
	{
		return (int) Math.round(x*y);
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		else if(obj == null)
			return false;
		else if(!(obj instanceof Vec2RO))
			return false;

		Vec2RO other = (Vec2RO) obj;
		if(x != other.x || (y != other.y))
			return false;
		return true;
	}

	/**
	 * Copie this dans other.
	 * 
	 * @param other
	 */
	public final void copy(Vec2RW other)
	{
		other.x = x;
		other.y = y;
	}

	public final Vec2RW rotateNewVector(double angle, Vec2RO centreRotation)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Vec2RW(cos * (x - centreRotation.x) - sin * (y - centreRotation.y) + centreRotation.x, sin * (x - centreRotation.x) + cos * (y - centreRotation.y) + centreRotation.y);
	}

	public final double getArgument()
	{
		return Math.atan2(y, x);
	}

	
	public final double getX()
	{
		return x;
	}

	public final double getY()
	{
		return y;
	}

	/**
	 * La norme du vecteur
	 * 
	 * @return
	 */
	public double norm()
	{
		return Math.sqrt(x * x + y * y);
	}

}
