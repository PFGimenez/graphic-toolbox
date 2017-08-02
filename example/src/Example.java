import java.awt.Color;

import pfg.graphic.DebugTool;
import pfg.graphic.Fenetre;
import pfg.graphic.PrintBuffer;
import pfg.graphic.Vec2RO;
import pfg.graphic.printable.Layer;
import pfg.graphic.printable.Segment;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

public class Example
{

	public static void main(String[] args) throws InterruptedException
	{
		Fenetre f = new DebugTool().getFenetre(new Vec2RO(0, 0));
		PrintBuffer buffer = f.getPrintBuffer();
		buffer.add(new Segment(new Vec2RO(-10, -10), new Vec2RO(10, 10), Layer.FOREGROUND, Color.RED));
		buffer.addSupprimable(new Segment(new Vec2RO(-20, 20), new Vec2RO(-20, -20), Layer.FOREGROUND, Color.BLUE));
		f.refresh();
		Thread.sleep(1000);
		buffer.clearSupprimables();
		f.refresh();
		Thread.sleep(2000);
	}

}
