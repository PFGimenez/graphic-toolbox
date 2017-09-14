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
	private Color c = new Color(0,50,50,50);
	
	@Override
	public void print(Graphics g, GraphicPanel f, Chart a)
	{
		g.setColor(c);
		Vec2RO basGauche = f.getCurrentCoinBasGauche();
		Vec2RO hautDroite = f.getCurrentCoinHautDroite();
		
		double pasX = Math.pow(10, Math.floor(Math.log10(hautDroite.getX() - basGauche.getX())));
		double startX = Math.ceil(basGauche.getX() / pasX) * pasX;
		for(int i = 0; i < 10; i++)
			g.drawLine(f.XtoWindow(startX + i*pasX), f.YtoWindow(basGauche.getY()), f.XtoWindow(startX + i*pasX), f.YtoWindow(hautDroite.getY()));

		double pasY = Math.pow(10, Math.floor(Math.log10(hautDroite.getY() - basGauche.getY())));
		double startY = Math.ceil(basGauche.getY() / pasY) * pasY;
		for(int i = 0; i < 10; i++)
			g.drawLine(f.XtoWindow(basGauche.getX()), f.YtoWindow(startY + i*pasY), f.XtoWindow(hautDroite.getX()), f.YtoWindow(startY + i*pasY));
	}

	@Override
	public int getLayer()
	{
		return Layer.IMAGE_BACKGROUND.ordinal();
	}

}
