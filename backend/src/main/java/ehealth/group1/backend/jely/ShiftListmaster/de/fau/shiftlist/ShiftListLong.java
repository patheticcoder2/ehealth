/**
 * This file is part of the ShiftList distribution (https://github.com/mad-lab-fau/ShiftList).
 * Copyright (c) 2015-2020 Machine Learning and Data Analytics Lab, Friedrich-Alexander-Universität Erlangen-Nürnberg (FAU).
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 * <p>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ehealth.group1.backend.jely.ShiftListmaster.de.fau.shiftlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A fast (circular) list of double values. If new doubles are added after the
 * maximal size has been reached the oldest entries are overwritten.
 * 
 * @author gradl
 *
 */
public class ShiftListLong extends ShiftList<Long> {
	private long[] mListBuffer;

	public ShiftListLong(int maxSize) {
		super(maxSize);
		mListBuffer = new long[maxSize];
	}

	/**
	 * Constructs a new ShiftListDouble that wraps(!) the given double-list. It does
	 * not copy the array!
	 * 
	 * @param list double array that is wrapped with a ShiftListDouble.
	 */
	public ShiftListLong(long[] list) {
		super(list.length);
		mListBuffer = list;
		mLifetimeSize = mListBuffer.length;
		mHeadIndex = (int) (mLifetimeSize - 1);
	}

	/**
	 * Copy constructor for generic (number) lists.
	 * 
	 * @param listToCopyFrom
	 */
	public ShiftListLong(List<? extends Number> listToCopyFrom) {
		this(listToCopyFrom.size());

		// check if the parameter is also a ShiftListDouble to allow fast copy
		ShiftList<?> list = null;
		if (listToCopyFrom instanceof ShiftList<?>) {
			list = (ShiftList<?>) listToCopyFrom;
			if (!(list instanceof ShiftListLong)) {
				list = null;
			}
		}

		if (list == null) {
			// no ShiftListDouble -> slow index-wise copy
			for (int i = 0; i < mListBuffer.length; i++) {
				mListBuffer[i] = (long) listToCopyFrom.get(i);
			}
		} else {
			// the parameter is also a ShiftListDouble
			ShiftListLong sl = (ShiftListLong) list;
			System.arraycopy(sl.mListBuffer, 0, mListBuffer, 0, sl.mListBuffer.length);
		}

		mLifetimeSize = mListBuffer.length;
		mHeadIndex = (int) (mLifetimeSize - 1);
	}

	@Override
	public boolean add(Long e) {
		beforeAddItem();
		mListBuffer[mHeadIndex] = e;
		return true;
	}

	@Override
	public void add(int index, Long element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Long> c) {
		for (Iterator<? extends Long> iterator = c.iterator(); iterator.hasNext();) {
			add((Long) iterator.next());
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Long> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		super.clear();
		Arrays.fill(mListBuffer, 0);
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long get(int index) {
		return mListBuffer[getNormalizedIndex(index)];
	}

	public double getHeadValue() {
		return mListBuffer[getHeadIndex()];
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<Long> iterator() {
		return new ShiftListLongIterator(this);
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Long> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<Long> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long set(int index, Long element) {
		if (index < getTailIndex())
			return element;
		mListBuffer[getNormalizedIndex(index)] = element;
		return element;
	}

	@Override
	public List<Long> subList(int fromIndex, int toIndex) {
		int size = toIndex - fromIndex;
		ArrayList<Long> list = new ArrayList<Long>(size);
		for (int i = fromIndex; i < toIndex; i++) {
			list.add(get(i));
		}
		return list;
	}

	@Override
	public Object[] toArray() {
		Object[] list = new Double[getMaxSize()];
		for (int i = 0; i < list.length; i++) {
			list[i] = mListBuffer[i];
		}
		return list;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	public long[] getBuffer() {
		return mListBuffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("[ ");
		for (int i = 0; i < mListBuffer.length; i++) {
			if (i > 0)
				str.append("; ");
			str.append(mListBuffer[i]);
		}
		str.append(" ]");
		return str.toString();
	}

	private class ShiftListLongIterator implements Iterator<Long> {
		private int mNextIndex = 0;
		private ShiftListLong mList;

		public ShiftListLongIterator(ShiftListLong list) {
			mList = list;
			mNextIndex = (int) mList.getTailIndex();
		}

		@Override
		public boolean hasNext() {
			return mNextIndex < mList.getLifetimeSize();
		}

		@Override
		public Long next() {
			return mList.get(mNextIndex++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public ShiftListMinMax<Long> getMinMax() {
		long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
		int minIndex = 0, maxIndex = 0;

		long cur = 0;
		for (int i = 0; i < mListBuffer.length; i++) {
			cur = mListBuffer[i];
			if (cur > max) {
				max = cur;
				maxIndex = i;
			}
			if (cur < min) {
				min = cur;
				minIndex = i;
			}
		}

		return new ShiftListMinMax<Long>(this, new Long(min), minIndex, new Long(max), maxIndex);
	}

}