/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 * Affichage des données de debug
 * @author pf
 *
 */

public class AffichageDebug extends ApplicationFrame
{
	private static final long serialVersionUID = 1L;
	private List<TimeSeries> series = new ArrayList<TimeSeries>();
    private TimeSeriesCollection dataset = new TimeSeriesCollection();
	private boolean init = false;
	private String title, xAxisLabel, yAxisLabel;
	
    /**
     * Ajoute des données à afficher
     * @param data
     * @param names
     * @throws InvalidParameterException
     */
	public void addData(double[] data, String[] names) throws InvalidParameterException
	{
		if(names.length != data.length)
			throw new InvalidParameterException();

		if(!init)
			init();
		
		Date temps = new Date();
		for(int i = 0; i < data.length; i++)
		{
			if(i == series.size())
			{
				TimeSeries tmp = new TimeSeries(names[i]);
	        	series.add(tmp);
	            dataset.addSeries(tmp);
			}
			series.get(i).add(new Millisecond(temps), data[i]);
		}
	}
	
    public AffichageDebug(String title, String xAxisLabel, String yAxisLabel)
    {    	
        super("");
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }    
    
    /**
     * L'initialisation se fait à part afin de ne pas ouvrir une fenêtre dès qu'on crée un objet
     */
    private void init()
    {
    	init = true;
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
        		title,  		// title
        		xAxisLabel,            // x-axis label
        		yAxisLabel,   		// y-axis label
        		dataset,            // data
        		true,               // create legend?
        		true,               // generate tooltips?
        		false               // generate URLs?
        		);

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer)
        {
        	XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
        	renderer.setBaseShapesVisible(true);
        	renderer.setBaseShapesFilled(true);
        	renderer.setDrawSeriesLineAsPath(true);
      	}
        
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(1024, 600));
        setContentPane(panel);

		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
    }

}