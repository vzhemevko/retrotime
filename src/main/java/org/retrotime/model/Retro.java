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
@Table(name="RETRO")
public class Retro {

    private int id;

    private String name;

    private boolean active;

    private Team team;

    private String actionItems;

    private Date createdAt;

    private Set<Content> contentRecords = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
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

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "retro", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    public Set<Content> getContentRecords() {
        return contentRecords;
    }

    public void setContentRecords(Set<Content> contentRecords) {
        this.contentRecords = contentRecords;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "TEAM_ID", nullable = true)
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Column(name = "ACTION_ITEMS", nullable = true, columnDefinition="TEXT")
    public String getActionItems() {
        return actionItems;
    }

    public void setActionItems(String actionItems) {
        this.actionItems = actionItems;
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
