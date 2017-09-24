/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.awt.Graphics;
import java.io.Serializable;

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
	private int taille = 2;
	
	public PrintablePoint(double x, double y)
	{
		this.a = new Vec2RO(x, y);
	}

	public PrintablePoint(double x, double y, int taille)
	{
		this.a = new Vec2RO(x, y);
		this.taille = taille;
	}

	public PrintablePoint(Position a)
	{
		this.a = new Vec2RO(a.getX(), a.getY());
	}

	@Override
	public void print(Graphics g, GraphicPanel f)
	{
		g.fillOval(f.XtoWindow(a.getX())-taille/2, f.YtoWindow(a.getY())-taille/2, taille, taille);
	}

	@Override
	public String toString()
	{
		return "Point en " + a;
	}
}
