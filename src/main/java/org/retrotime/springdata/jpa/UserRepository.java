package org.retrotime.springdata.jpa;

import org.retrotime.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface UserRepository extends FatherRepository<User, Serializable> {

    User findUserByNameAndPasswd(String username, String passwd);

    User findUserByName(String name);

    List<User> findByRole(int role);

    @Query(value = "Select * from users u inner join user_team ut "
                    + "on u.id = ut.user_id where ut.team_id = :teamId", nativeQuery=true)
    List<User> findByTeamId(@Param(value = "teamId") int teamId);

    List<User> findByPersonalNameContainingIgnoreCase(String query);
}
