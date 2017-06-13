/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic.printable;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import graphic.Fenetre;
import graphic.Position;
import graphic.Vec2RO;

/**
 * Un segment affichable
 * 
 * @author pf
 *
 */

public class Segment implements Printable, Serializable
{
	private static final long serialVersionUID = 3887897521575363643L;
	private Vec2RO a, b;
	private Layer l;
	private Color c;
	
	@Override
	public Segment clone()
	{
		return new Segment(a.clone(), b.clone(), l, c);
	}

	public Segment(Position a, Position b, Layer l, Color c)
	{
		this.a = new Vec2RO(a.getX(), a.getY());
		this.b = new Vec2RO(b.getX(), b.getY());
		this.l = l;
		this.c = c;
	}

	@Override
	public void print(Graphics g, Fenetre f)
	{
		System.out.println("Positions écran : "+f.XtoWindow(a.getX())+" "+f.YtoWindow(a.getY())+" "+f.XtoWindow(b.getX())+" "+f.YtoWindow(b.getY()));
		g.setColor(c);
		g.drawLine(f.XtoWindow(a.getX()), f.YtoWindow(a.getY()), f.XtoWindow(b.getX()), f.YtoWindow(b.getY()));
	}

	@Override
	public int getLayer()
	{
		return l.ordinal();
	}

	@Override
	public String toString()
	{
		return "Segment entre " + a + " et " + b;
	}

	public void setColor(Layer l, Color c)
	{
		this.l = l;
		this.c = c;
	}

}
