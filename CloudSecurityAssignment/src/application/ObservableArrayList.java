package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ObservableArrayList<T> implements ObservableList<T> {
	private ArrayList<T> list;
	
	public ObservableArrayList(ArrayList<T> list) {
		this.list = list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object object) {
		return list.contains(object);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return list.add(e);
	}

	@Override
	public boolean remove(Object object) {
		return list.remove(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return list.containsAll(collection);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		return list.addAll(collection);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection) {
		return list.addAll(index, collection);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return list.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return list.retainAll(collection);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public T set(int index, T element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		list.add(index, element);
	}

	@Override
	public T remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object object) {
		return list.indexOf(object);
	}

	@Override
	public int lastIndexOf(Object object) {
		return list.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public void addListener(InvalidationListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(InvalidationListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addAll(T... arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addListener(ListChangeListener<? super T> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeAll(T... arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ListChangeListener<? super T> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean retainAll(T... arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setAll(T... arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setAll(Collection<? extends T> arg0) {
		// TODO Auto-generated method stub
		return false;
	}}
