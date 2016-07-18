package org.retrotime.springdata.jpa;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface FatherRepository<T, ID extends Serializable> extends Repository<T, ID> {

    void delete(T deleted);

    void deleteById(int id);

    List<T> findAll();

    Optional<T> findOne(ID id);

    T save(T persisted);
}
