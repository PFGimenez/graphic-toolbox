/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.awt.Color;
import java.awt.Graphics;

import pfg.graphic.Chart;
import pfg.graphic.GraphicPanel;
import pfg.graphic.Vec2RO;

/**
 * A background grid
 * @author pf
 *
 */

public class BackgroundGrid implements Printable
{
	private static final long serialVersionUID = 1422929627673510227L;
	private Color c = new Color(200,200,200);
	
	@Override
	public void print(Graphics g, GraphicPanel f, Chart a)
	{
		g.setColor(c);
		Vec2RO basGauche = f.getCurrentCoinBasGauche();
		Vec2RO hautDroite = f.getCurrentCoinHautDroite();
		
		double pasX = Math.pow(10, Math.floor(Math.log10(hautDroite.getX() - basGauche.getX()))-1);
		double startX = Math.ceil(basGauche.getX() / pasX) * pasX;
		int i = 0;
		while(startX + i*pasX <= hautDroite.getX())
		{
			g.drawLine(f.XtoWindow(startX + i*pasX), f.YtoWindow(basGauche.getY()), f.XtoWindow(startX + i*pasX), f.YtoWindow(hautDroite.getY()));
			i++;
		}

		double pasY = Math.pow(10, Math.floor(Math.log10(hautDroite.getY() - basGauche.getY()))-1);
		double startY = Math.ceil(basGauche.getY() / pasY) * pasY;
		i = 0;
		while(startY + i*pasY <= hautDroite.getY())
		{
			g.drawLine(f.XtoWindow(basGauche.getX()), f.YtoWindow(startY + i*pasY), f.XtoWindow(hautDroite.getX()), f.YtoWindow(startY + i*pasY));
			i++;
		}
	}

	@Override
	public int getLayer()
	{
		return Layer.IMAGE_BACKGROUND.ordinal();
	}

}
