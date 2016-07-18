package org.retrotime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vzhemevko on 5/23/2015.
 */
@Entity
@Table(name = "TEAM")
public class Team {

    private int id;

    private String name;

    private boolean active;

    private Date createdAt;

    /*
    * At this point Release concept implementation is suspended.
    *
    * private Set<Release> releases = new HashSet<Release>(0);
    *
    */

    private Set<User> users = new HashSet<User>(0);
    private Set<Retro> retro = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, unique = true)
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

    /*
     * *********************************************************
     * At this point Release concept implementation is suspended
     * *********************************************************
     */

   /* @ManyToMany(fetch = FetchType.LAZY, mappedBy = "teams")
    public Set<Release> getReleases() {
        return releases;
    }*/

    /*public void setReleases(Set<Release> releases) {
        this.releases = releases;
    }*/

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "user_team", joinColumns = {
            @JoinColumn(name = "TEAM_ID", nullable = true, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "USER_ID",
                    nullable = true, updatable = true) })
    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "team", cascade = { CascadeType.REMOVE })
    public Set<Retro> getSprintRetro() {
        return retro;
    }

    public void setSprintRetro(Set<Retro> retro) {
        this.retro = retro;
    }

    @Column(name = "CREATED_AT")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void createdAt() {
      this.createdAt = new Date();
    }
}
