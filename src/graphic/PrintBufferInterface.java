/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 */

package graphic;

import graphic.printable.Layer;
import graphic.printable.Printable;

/**
 * Une interface qui permet de regrouper le print buffer déporté et celui qui ne
 * l'est pas
 * 
 * @author pf
 *
 */

public interface PrintBufferInterface
{

	public void clearSupprimables();

	public void addSupprimable(Printable o);

	public void addSupprimable(Printable o, Layer l);

	public void add(Printable o);

	public void removeSupprimable(Printable o);

	public void destructor();

}
