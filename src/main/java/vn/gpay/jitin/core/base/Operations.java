package vn.gpay.jitin.core.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public interface Operations<T extends Serializable> {

    // read - one

    T findOne(final long id);

    // read - all

    List<T> findAll();

    Page<T> findPaginated(int page, int size);

    // write

    T create(final T entity);
    
    T save(final T entity);
    
    T update(final T entity);

    void delete(final T entity);

    void deleteById(final long entityId);

	T saveandflush(T entity);

}
