package finance.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import finance.repository.errors.SuchElementOccurenceException;

public interface Repository<K, E> {
    E put(E entity) throws SuchElementOccurenceException;

    Iterable<E> putAll(Iterable<E> entities);

    E find(K id) throws NoSuchElementException;

    List<E> find(Set<K> id);

    Iterable<E> all();

    E update(E entity);

    E pull(K id);
}
