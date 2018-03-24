import java.awt.Color;

import pfg.graphic.DebugTool;
import pfg.graphic.GraphicDisplay;
import pfg.graphic.Vec2RO;
import pfg.graphic.Vec2RW;
import pfg.graphic.WindowFrame;
import pfg.graphic.printable.Layer;
import pfg.graphic.printable.Segment;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

/**
 * Ce serveur va juste afficher une animation en boucle
 * @author pf
 *
 */

public class ExternalDisplayServer
{

	public static void main(String[] args) throws InterruptedException
	{
		DebugTool dt = DebugTool.getDebugTool(new Vec2RO(0, 0), null, "graphic.conf", "default");
		WindowFrame f = dt.getWindowFrame();
		GraphicDisplay buffer = f.getPrintBuffer();
		Segment s1 = new Segment(new Vec2RO(-10, -10), new Vec2RO(10, 10));
		buffer.addPrintable(s1, Color.RED, Layer.FOREGROUND.layer);
		Segment s2 = new Segment(new Vec2RO(-20, 20), new Vec2RO(-20, -20));
		buffer.addTemporaryPrintable(s2, Color.BLUE, Layer.FOREGROUND.layer);
		Vec2RW tmp = new Vec2RW(10, 0, true);
		dt.startPrintServer();
		while(true)
		{
			tmp.rotate(0.1);
			s1.a.copy(s1.b);
			s1.b.plus(tmp);
			s2.a.copy(s2.b);
			s2.b.minus(tmp);
			f.refresh();
			Thread.sleep(50);
		}
	}

}
