/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Une liste de listes d'objets timestampées
 * 
 * @author pf
 *
 */

public class TimestampedList implements Serializable
{
	private static final long serialVersionUID = -5167892162649965305L;
	private List<Long> timestamps = new ArrayList<Long>();
	private List<List<Serializable>> listes = new ArrayList<List<Serializable>>();
	private long dateInitiale;

	public TimestampedList(long dateInitiale)
	{
		this.dateInitiale = dateInitiale;
	}

	public void add(List<Serializable> o)
	{
		timestamps.add(System.currentTimeMillis() - dateInitiale);
		listes.add(o);
	}

	public long getTimestamp(int index)
	{
		return timestamps.get(index);
	}

	public List<Serializable> getListe(int index)
	{
		return listes.get(index);
	}

	public int size()
	{
		return timestamps.size();
	}

}
