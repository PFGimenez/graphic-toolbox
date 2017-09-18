/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pfg.config.Config;

/**
 * Affichage d'une console
 * @author pf
 *
 */

public class ConsoleDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextArea texte;
	
	public ConsoleDisplay(Config config)
	{
		texte = new JTextArea(config.getInt(ConfigInfoGraphic.CONSOLE_NB_ROWS), config.getInt(ConfigInfoGraphic.CONSOLE_NB_COLUMNS));
		texte.setEditable(false);
		texte.setBackground(Color.WHITE);
		texte.setLineWrap(true);
		texte.setWrapStyleWord(true);
		add(texte);
	}

	public void write(String str)
	{
		texte.append(str);
	}
	
}
