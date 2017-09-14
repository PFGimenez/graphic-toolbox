/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import pfg.graphic.Chart;
import pfg.graphic.GraphicPanel;
import pfg.graphic.Position;
import pfg.graphic.Vec2RO;

/**
 * Un point
 * 
 * @author pf
 *
 */

public class PrintablePoint implements Printable, Serializable
{
	private static final long serialVersionUID = 3887897521575363643L;
	private Vec2RO a;
	private Layer l;
	private Color c;
	private int taille = 2;
	
	public PrintablePoint(double x, double y, Layer l, Color c)
	{
		this.a = new Vec2RO(x, y);
		this.l = l;
		this.c = c;
	}
	
	public PrintablePoint(Position a, Layer l, Color c)
	{
		this.a = new Vec2RO(a.getX(), a.getY());
		this.l = l;
		this.c = c;
	}

	@Override
	public void print(Graphics g, GraphicPanel f, Chart aff)
	{
		g.setColor(c);
		g.fillOval(f.XtoWindow(a.getX())-taille/2, f.YtoWindow(a.getY())-taille/2, taille, taille);
	}

	@Override
	public int getLayer()
	{
		return l.ordinal();
	}

	@Override
	public String toString()
	{
		return "Point en " + a;
	}

	public void setColor(Layer l, Color c)
	{
		this.l = l;
		this.c = c;
	}

}
