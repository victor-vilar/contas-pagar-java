package br.com.victorvilar.contaspagar.services.interfaces;

import java.util.List;

public interface AppService<T> {

	
	public List<T> getTodos();
	public T getById(Long id);
	public T save(T obj);
	public List<T> saveAll(List<T> objs);
	public T update(T obj);
	public void deleteById(Long id);
	public void deleteAll(List<T> objs);
	
}
