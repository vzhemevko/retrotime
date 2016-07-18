package org.retrotime.springdata.jpa;

import java.io.Serializable;
import java.util.List;

import org.retrotime.model.Team;

public interface TeamRepository extends FatherRepository<Team, Serializable> {

    Team findByNameOrderByCreatedAtDesc(String name);

    List<Team> findAllByOrderByCreatedAtDesc();
}
