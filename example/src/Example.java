import java.awt.Color;
import graphic.Fenetre;
import graphic.PrintBuffer;
import graphic.Vec2RO;
import graphic.printable.Layer;
import graphic.printable.Segment;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

public class Example
{

	public static void main(String[] args) throws InterruptedException
	{
		Fenetre f = new Fenetre(new Vec2RO(0,0));
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
