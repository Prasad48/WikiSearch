package com.bhavaniprasad.wikisearch.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class wikiUsersList implements Serializable {

    private String title;
    private List<String> description;
    @SerializedName("query")
    private wikiUsersList query;
    @SerializedName("pages")
    private List<wikiUsersList> pages;
    private wikiUsersList thumbnail;
    private String source;
    private wikiUsersList terms;

    public wikiUsersList(String title, List<String> description, wikiUsersList avatar) {
        this.title=title;
        this.description=description;
        this.thumbnail=avatar;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public wikiUsersList getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(wikiUsersList thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public wikiUsersList getTerms() {
        return terms;
    }

    public void setTerms(wikiUsersList terms) {
        this.terms = terms;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<wikiUsersList> getPages() {
        return pages;
    }

    public void setPages(List<wikiUsersList> pages) {
        this.pages = pages;
    }

    public wikiUsersList getQuery() {
        return query;
    }

    public void setQuery(wikiUsersList query) {
        this.query = query;
    }
}
