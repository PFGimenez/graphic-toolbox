/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 */

package graphic.printable;

import java.awt.Graphics;
import java.awt.Image;
import graphic.Fenetre;

/**
 * Image de fond
 * 
 * @author pf
 *
 */

public class BackgroundImage implements Printable
{
	private Image image;

	public BackgroundImage(Image image)
	{
		this.image = image;
	}

	@Override
	public void print(Graphics g, Fenetre f)
	{
		g.drawImage(image, f.XtoWindow(-1500), f.YtoWindow(2000), f.distanceXtoWindow(3000), f.distanceYtoWindow(2000), f);
	}

	@Override
	public Layer getLayer()
	{
		return Layer.IMAGE_BACKGROUND;
	}

}
