import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

import pfg.graphic.Chart;
import pfg.graphic.DebugTool;
import pfg.graphic.GraphicDisplay;
import pfg.graphic.Vec2RO;
import pfg.graphic.WindowFrame;
import pfg.graphic.printable.Layer;
import pfg.graphic.printable.Plottable;
import pfg.graphic.printable.Segment;
import pfg.log.Log;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

public class Example
{

	public static void main(String[] args) throws InterruptedException
	{
		DebugTool dt = DebugTool.getDebugTool(null);
		WindowFrame f = dt.getWindowFrame(new Vec2RO(0, 0));
		Log log = dt.getLog();
		GraphicDisplay buffer = f.getPrintBuffer();
		Segment s1 = new Segment(new Vec2RO(-10, -10), new Vec2RO(10, 10));
		buffer.addPrintable(s1, Color.RED, Layer.FOREGROUND.layer);
		Segment s2 = new Segment(new Vec2RO(-20, 20), new Vec2RO(-20, -20));
		buffer.addTemporaryPrintable(s2, Color.BLUE, Layer.FOREGROUND.layer);
		log.write("Test !", null);
		f.refresh();
		Thread.sleep(1000);
		buffer.clearTemporaryPrintables();
		f.refresh();
		Thread.sleep(1000);
		buffer.addPlottable(new RandomValue("Test 1"));
		buffer.addPlottable(new RandomValue("Test 2"));
		for(int i = 0; i < 10; i++)
		{
			f.refresh();
			Thread.sleep(200);
		}
	}
	
	public static class RandomValue implements Plottable
	{
		private static final long serialVersionUID = 1L;
		private int value = 0;
		private Random r = new Random();
		private String nom;
		
		public RandomValue(String nom)
		{
			this.nom = nom;
		}

		@Override
		public void plot(Chart a)
		{
			HashMap<String, Double> values = new HashMap<String, Double>();
			values.put(nom, (double) value);
			a.addData(values);
			value += r.nextInt(5) - 2;
		}
	}

}
