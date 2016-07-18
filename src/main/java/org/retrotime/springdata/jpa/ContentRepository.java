package org.retrotime.springdata.jpa;

import org.retrotime.model.Content;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface ContentRepository extends FatherRepository<Content, Serializable> {

    @Query(value = "Select * from content where retro_id = :retroId and user_id = :userId", nativeQuery=true)
    List<Content> findContForUserInRetro(@Param(value = "retroId") int retroId, @Param(value = "userId") int userId);
}
