package org.retrotime.model;

import javax.persistence.*;

/**
 * Created by vzhemevko on 5/22/2015.
 */
@Entity
@Table(name = "CONTENT")
public class Content {

    private int id;

    private String www; // What went well

    private String wcwi; // What can we improve

    private String kudos;

    private User user;

    private Retro retro;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "WCWI", nullable = true, columnDefinition="TEXT")
    public String getWcwi() {
        return wcwi;
    }

    public void setWcwi(String wcwi) {
        this.wcwi = wcwi;
    }

    @Column(name = "WWW", nullable = true, columnDefinition="TEXT")
    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    @Column(name = "KUDOS", nullable = true, columnDefinition="TEXT")
    public String getKudos() {
        return kudos;
    }

    public void setKudos(String kudos) {
        this.kudos = kudos;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "RETRO_ID", nullable = false)
    public Retro getRetro() {
        return retro;
    }

    public void setRetro(Retro retro) {
        this.retro = retro;
    }
}
