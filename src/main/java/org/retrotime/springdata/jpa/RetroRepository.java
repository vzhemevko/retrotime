package org.retrotime.springdata.jpa;

import org.retrotime.model.Retro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface RetroRepository extends FatherRepository<Retro, Serializable> {

    List<Retro> findByTeamIdOrderByCreatedAtDesc(int id);

    void deleteByIdAndTeamId(int retroId, int teamId);

    @Query(value = "Update retro set action_items = :text where id = :id", nativeQuery=true)
    void updateActionItems(@Param(value= "text") String text, @Param(value = "id") int id);

}
