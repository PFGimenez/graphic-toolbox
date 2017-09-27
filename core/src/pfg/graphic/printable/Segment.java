/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.awt.Graphics;
import java.io.Serializable;

import pfg.graphic.GraphicPanel;
import pfg.graphic.Position;
import pfg.graphic.Vec2RW;

/**
 * Un segment affichable
 * 
 * @author pf
 *
 */

public class Segment implements Printable, Serializable
{
	private static final long serialVersionUID = 3887897521575363643L;
	public Vec2RW a, b;

	public Segment(Position a, Position b)
	{
		this.a = new Vec2RW(a.getX(), a.getY());
		this.b = new Vec2RW(b.getX(), b.getY());
	}

	@Override
	public void print(Graphics g, GraphicPanel f)
	{
		g.drawLine(f.XtoWindow(a.getX()), f.YtoWindow(a.getY()), f.XtoWindow(b.getX()), f.YtoWindow(b.getY()));
	}
	@Override
	public String toString()
	{
		return "Segment entre " + a + " et " + b;
	}

}
