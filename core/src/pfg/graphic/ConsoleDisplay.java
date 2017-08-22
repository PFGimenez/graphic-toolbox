/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Affichage d'une console
 * @author pf
 *
 */

public class ConsoleDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextArea texte;
	
	public ConsoleDisplay(int rows, int columns)
	{
		texte = new JTextArea(rows, columns);
	}

	public void write(String str)
	{
		texte.append(str);
	}
	
}
