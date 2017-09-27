import java.util.HashMap;
import java.util.Random;

import pfg.graphic.Chart;
import pfg.graphic.DebugTool;
import pfg.graphic.Vec2RO;
import pfg.graphic.printable.Plottable;

/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

public class ExternalDisplayClient
{

	public static void main(String[] args) throws InterruptedException
	{
		DebugTool dt = DebugTool.getDebugTool(null);
		dt.getWindowFrame(new Vec2RO(0, 0));
		dt.startAutomaticRefresh();
		dt.startPrintClient("127.0.0.1");
		while(true)
			Thread.sleep(1000);
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
