/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 */

package graphic.printable;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import graphic.Fenetre;
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

	public Segment(Vec2RO a, Vec2RO b, Couleur c)
	{
		this.a = a;
		this.b = b;
		this.l = c.l;
		this.c = c.couleur;
	}
	
	@Override
	public Segment clone()
	{
		return new Segment(a.clone(), b.clone(), l, c);
	}

	public Segment(Vec2RO a, Vec2RO b, Layer l, Color c)
	{
		this.a = a;
		this.b = b;
		this.l = l;
		this.c = c;
	}

	@Override
	public void print(Graphics g, Fenetre f)
	{
		g.setColor(c);
		g.drawLine(f.XtoWindow(a.getX()), f.YtoWindow(a.getY()), f.XtoWindow(b.getX()), f.YtoWindow(b.getY()));
	}

	@Override
	public Layer getLayer()
	{
		return l;
	}

	@Override
	public String toString()
	{
		return "Segment entre " + a + " et " + b;
	}

	public void setColor(Couleur c)
	{
		this.l = c.l;
		this.c = c.couleur;
	}

}
