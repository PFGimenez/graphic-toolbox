package pfg.graphic.printable;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import pfg.graphic.GraphicPanel;

/**
 * A printable with its color
 * @author pf
 *
 */

public class ColoredPrintable implements Serializable
{
	private static final long serialVersionUID = 1007485107824898388L;
	public final Printable p;
	public final Color c;
	public final int l;
	
	public ColoredPrintable(Printable p, Color c, int l)
	{
		this.p = p;
		this.c = c;
		this.l = l;
	}
	
	public void print(Graphics g, GraphicPanel f)
	{
		if(c != null)
			g.setColor(c);
		p.print(g, f);
	}

}
