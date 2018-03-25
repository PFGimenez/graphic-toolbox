/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import pfg.graphic.printable.ColoredPrintable;

/**
 * Une liste de listes d'objets timestampées
 * 
 * @author pf
 *
 */

public class TimestampedList implements Serializable
{
	private static final long serialVersionUID = -5167892162649965305L;
	private final List<Long> listesTimestamped = new ArrayList<Long>();
	private final List<byte[]> listes = new ArrayList<byte[]>();
//	private final List<byte[]> listesPlottable = new ArrayList<byte[]>();
	private transient long dateInitiale;
	private transient ByteArrayOutputStream array;

	public TimestampedList(long dateInitiale)
	{
		array = new ByteArrayOutputStream();
		this.dateInitiale = dateInitiale;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized PriorityQueue<ColoredPrintable> getList(int indexList)
	{
		try {
			byte b[] = listes.get(indexList);
			ByteArrayInputStream array = new ByteArrayInputStream(b);
			ObjectInputStream input = new ObjectInputStream(array);
			Object o = input.readObject();
//			PriorityQueue<ColoredPrintable> o = (PriorityQueue<ColoredPrintable>) input.readObject();
			return (PriorityQueue<ColoredPrintable>) o;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			assert false : e;
		}
		return null;
	}

	/**
	 * On sérialise directement en byte[], ce qui fait donc une copie de l'objet à l'instant où cette méthode est appelée
	 * @param o
	 */
	public synchronized void add(PriorityQueue<ColoredPrintable> o)
	{
		try {
			listesTimestamped.add(System.currentTimeMillis() - dateInitiale);
			ObjectOutputStream tmp = new ObjectOutputStream(array);
			tmp.writeObject(o);
			tmp.flush();
			listes.add(array.toByteArray());
			array.reset();
			
/*			tmp.writeObject(p);
			tmp.flush();
			listesPlottable.add(array.toByteArray());
			array.reset();*/
			
			assert listesTimestamped.size() == listes.size();// && listesTimestamped.size() == listesPlottable.size();
		} catch (IOException e) {
			e.printStackTrace(); // Impossible
			assert false : e;
		}
	}

	public long getTimestamp(int i)
	{
		return listesTimestamped.get(i);
	}

	public int size()
	{
		assert listesTimestamped.size() == listes.size();// && listesTimestamped.size() == listesPlottable.size();
		return listesTimestamped.size();
	}

	public boolean isEmpty()
	{
		assert listesTimestamped.isEmpty() == listes.isEmpty();// && listesTimestamped.isEmpty() == listesPlottable.isEmpty();
		return listesTimestamped.isEmpty();
	}

}
