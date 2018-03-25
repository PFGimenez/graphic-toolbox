import java.awt.Color;
import java.io.IOException;
import pfg.graphic.DebugTool;
import pfg.graphic.GraphicDisplay;
import pfg.graphic.Vec2RO;
import pfg.graphic.WindowFrame;
import pfg.graphic.printable.Layer;
import pfg.graphic.printable.Segment;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

public class ExampleVideo
{

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException
	{
		Segment s1 = new Segment(new Vec2RO(-10, -10), new Vec2RO(10, 10));
		DebugTool dt = DebugTool.getDebugTool(s1.a, null, "graphic.conf", "default");
		WindowFrame f = dt.getWindowFrame();
		GraphicDisplay buffer = f.getPrintBuffer();
		dt.startSaveVideo();
		double angle = 0;
		buffer.addPrintable(s1, Color.RED, Layer.FOREGROUND.layer);		
		f.refresh();
		for(int i = 0; i < 30; i++)
		{
			s1.a.set(10, angle);
			s1.b.set(10, angle + Math.PI);
			f.refresh();
			Thread.sleep(200);
			angle += 0.1;
		}
		dt.destructor();
	}

}
