import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Random;

import pfg.graphic.Chart;
import pfg.graphic.DebugTool;
import pfg.graphic.GraphicPanel;
import pfg.graphic.PrintBuffer;
import pfg.graphic.Vec2RO;
import pfg.graphic.WindowFrame;
import pfg.graphic.printable.Layer;
import pfg.graphic.printable.Printable;
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
		DebugTool dt = new DebugTool(null);
		WindowFrame f = dt.getWindowFrame(new Vec2RO(0, 0));
		Log log = dt.getLog();
		PrintBuffer buffer = f.getPrintBuffer();
		Segment s1 = new Segment(new Vec2RO(-10, -10), new Vec2RO(10, 10), Layer.FOREGROUND, Color.RED);
		buffer.add(s1);
		Segment s2 = new Segment(new Vec2RO(-20, 20), new Vec2RO(-20, -20), Layer.FOREGROUND, Color.BLUE);
		buffer.addSupprimable(s2);
		log.write("Test !", null);
		f.refresh();
		Thread.sleep(1000);
		buffer.clearSupprimables();
		f.refresh();
		Thread.sleep(1000);
		buffer.add(new RandomValue("Test 1"));
		buffer.add(new RandomValue("Test 2"));
		for(int i = 0; i < 10; i++)
		{
			f.refresh();
			Thread.sleep(200);
		}
	}
	
	public static class RandomValue implements Printable
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
		public void print(Graphics g, GraphicPanel f, Chart a)
		{
			HashMap<String, Double> values = new HashMap<String, Double>();
			values.put(nom, (double) value);
			a.addData(values);
			value += r.nextInt(5) - 2;
		}

		@Override
		public int getLayer()
		{
			return 0;
		}
	}

}