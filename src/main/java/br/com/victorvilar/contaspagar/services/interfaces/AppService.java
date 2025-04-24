package br.com.victorvilar.contaspagar.services.interfaces;

import jakarta.transaction.Transactional;
import java.util.List;

public interface AppService<T> {

	
	public List<T> getTodos();
	public T getById(Long id);
        @Transactional
	public T save(T obj);
	@Transactional
        public List<T> saveAll(List<T> objs);
	@Transactional
        public T update(T obj);
	@Transactional
        public void deleteById(Long id);
	@Transactional
        public void deleteAll(List<T> objs);
	
}
