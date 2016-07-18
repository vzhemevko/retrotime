package org.retrotime.model;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vzhemevko on 5/23/2015.
 */
/*@Entity
@Table(name = "REALEASE")
*
* *********************************************************
* At this point Release concept implementation is suspended.
* *********************************************************
*/
public class Release {

    private int id;

    private String name;

    private boolean active;

    private Set<Team> teams = new HashSet<Team>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RELEASE_ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ACTIVE", nullable = false)
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "release_team", joinColumns = {
            @JoinColumn(name = "RELEASE_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "TEAM_ID",
                    nullable = false, updatable = false) })
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
