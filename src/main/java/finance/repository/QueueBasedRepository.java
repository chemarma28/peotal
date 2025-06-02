package finance.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import finance.repository.errors.SuchElementOccurenceException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QueueBasedRepository<K, T extends Entity<K>> implements Repository<K, T> {
    private Queue<T> storage = new ConcurrentLinkedQueue<>();

    public QueueBasedRepository(List<T> entities) {
        storage.addAll(entities);
    }

    @Override
    public T put(T entity) throws SuchElementOccurenceException {
        if (null == entity.id())
            throw new UnsupportedOperationException("Repository doesn't support nullable ids.");

        Optional<T> container = storage.stream().filter(e -> e.id().equals(entity.id())).findFirst();

        if (container.isPresent())
            throw new SuchElementOccurenceException(entity);

        if (storage.add(entity))
            return entity;
        else
            return null;
    }

    @Override
    public Iterable<T> putAll(Iterable<T> entities) {
        List<T> result = new ArrayList<>();

        for (T entity : entities)
            result.add(entity);

        storage.addAll(result);

        return result;
    }

    @Override
    public T find(K id) throws NoSuchElementException {
        Optional<T> container = storage.stream().filter(e -> e.id().equals(id)).findFirst();

        return container.orElseThrow(
                () -> new NoSuchElementException(String.format("No entity with id='%s' is found.", id)));
    }

    @Override
    public List<T> find(Set<K> id) {
        return storage.stream().filter(e -> id.contains(e.id())).toList();
    }

    @Override
    public Iterable<T> all() {
        return storage;
    }

    @Override
    public T update(T entity) {
        if (entity.id() == null)
            return null;

        Optional<T> container = storage.stream().filter(e -> e.id().equals(entity.id())).findFirst();

        if (container.isEmpty()) {
            storage.add(entity);

            return entity;
        } else {
            T e = container.get();
            e = entity;

            return e;
        }
    }

    @Override
    public T pull(K id) {
        T e = find(id);

        storage.remove(e);

        return e;
    }
}
