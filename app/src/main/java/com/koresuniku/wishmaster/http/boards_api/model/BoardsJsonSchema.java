package com.koresuniku.wishmaster.http.boards_api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.koresuniku.wishmaster.http.IBaseJsonSchema;

import java.util.List;

public class BoardsJsonSchema implements IBaseJsonSchema {

    @SerializedName("Взрослым")
    @Expose
    private List<Adults> adults = null;
    @SerializedName("Игры")
    @Expose
    private List<Games> games = null;
    @SerializedName("Политика")
    @Expose
    private List<Politics> politics = null;
    @SerializedName("Пользовательские")
    @Expose
    private List<Users> users = null;
    @SerializedName("Разное")
    @Expose
    private List<Other> other = null;
    @SerializedName("Творчество")
    @Expose
    private List<Creativity> creativity = null;
    @SerializedName("Тематика")
    @Expose
    private List<Subjects> subject = null;
    @SerializedName("Техника и софт")
    @Expose
    private List<Tech> tech = null;
    @SerializedName("Японская культура")
    @Expose
    private List<Japanese> japanese = null;

    public List<Adults> getAdults() {
        return adults;
    }

    public void setAdults(List<Adults> adults) {
        this.adults = adults;
    }

    public List<Games> getGames() {
        return games;
    }

    public void setGames(List<Games> games) {
        this.games = games;
    }

    public List<Politics> getPolitics() {
        return politics;
    }

    public void setPolitics(List<Politics> politics) {
        this.politics = politics;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Other> getOther() {
        return other;
    }

    public void setOther(List<Other> other) {
        this.other = other;
    }

    public List<Creativity> getCreativity() {
        return creativity;
    }

    public void setCreativity(List<Creativity> creativity) {
        this.creativity = creativity;
    }

    public List<Subjects> getSubject() {
        return subject;
    }

    public void setSubject(List<Subjects> subject) {
        this.subject = subject;
    }

    public List<Tech> getTech() {
        return tech;
    }

    public void setTech(List<Tech> tech) {
        this.tech = tech;
    }

    public List<Japanese> getJapanese() {
        return japanese;
    }

    public void setJapanese(List<Japanese> japanese) {
        this.japanese = japanese;
    }

}