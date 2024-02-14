package com.ferreusveritas.api.hashlist;

import java.util.*;

/**
 * A list with hash-based index lookup.
 * <p>
 * @param <T>
 */
public class HashList<T> {
	
	private final Map<T, Integer> map = new HashMap<>();
	private final List<T> list = new ArrayList<>();
	
	public HashList() {
	}
	
	public HashList(HashList<T> hashList) {
		map.putAll(hashList.map);
		list.addAll(hashList.list);
	}
	
	public HashList(Collection<T> collection) {
		collection.forEach(this::add);
	}
	
	public int add(T t) {
		return map.computeIfAbsent(t, k -> {
			int pos = list.size();
			list.add(t);
			return pos;
		});
	}
	
	public Optional<Integer> get(T t) {
		return Optional.ofNullable(map.get(t));
	}
	
	public Optional<T> get(int index) {
		return Optional.ofNullable(list.get(index));
	}
	
	public int size() {
		return list.size();
	}
	
	public List<T> getList() {
		return List.copyOf(list);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(obj instanceof HashList<?> other) {
			return Objects.equals(list, other.list);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(list);
	}
	
}
