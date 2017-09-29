/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.util.HashMap;

import pfg.config.Config;
import pfg.config.ConfigInfo;
import pfg.injector.Injector;
import pfg.injector.InjectorException;
import pfg.log.Log;
import pfg.log.SeverityCategory;

/**
 * The debug tool
 * @author pf
 *
 */

public class DebugTool
{
	private Injector injector;
	private static DebugTool instance = null;
	
	public static DebugTool getDebugTool(Position center, SeverityCategory cat)
	{
		if(instance == null)
			instance = new DebugTool(new HashMap<ConfigInfo, Object>(), center, cat, "graphic.conf", "default");
		return instance;
	}
	
	public static DebugTool getExistingDebugTool()
	{
		return instance;
	}
	
	public static DebugTool getDebugTool(HashMap<ConfigInfo, Object> override, Position center, SeverityCategory cat, String configFilename, String... configprofile)
	{
		if(instance == null)
			instance = new DebugTool(override, center, cat, configFilename, configprofile);
		return instance;
	}
	
	private DebugTool(HashMap<ConfigInfo, Object> override, Position center, SeverityCategory cat, String configFilename, String... configprofile)
	{
		Config config = new Config(ConfigInfoGraphic.values(), false, configFilename, configprofile);
		config.override(override);
		injector = new Injector();
		injector.addService(config);
		Log log;
		try {
			log = Log.getLog(cat);
			log.addConsoleDisplay(injector.getService(ConsoleDisplay.class));
			log.useConfig(config);
			injector.addService(log);
			WindowFrame fenetre;
			GraphicDisplay gd = injector.getService(GraphicDisplay.class);
			GraphicPanel g = new GraphicPanel(center, config, gd);
			injector.addService(g);
			fenetre = injector.getService(WindowFrame.class);
			injector.addService(fenetre);
		} catch (InjectorException e) {
			e.printStackTrace();
		}
	}

	public void startPrintClient(String hostname)
	{
		try {
			ThreadPrintClient th = injector.getService(ThreadPrintClient.class);
			th.setHostname(hostname);
			th.start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startPrintServer()
	{
		try {
			injector.getService(ThreadPrintServer.class).start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startSaveVideo()
	{
		try {
			injector.getService(ThreadSaveVideo.class).start();;
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startAutomaticRefresh()
	{
		try {
			injector.getService(ThreadPrinting.class).start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public WindowFrame getWindowFrame()
	{
		return injector.getExistingService(WindowFrame.class);
	}
	
	public Log getLog()
	{
		return injector.getExistingService(Log.class);
	}

}
