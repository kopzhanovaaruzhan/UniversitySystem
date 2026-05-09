package models;

import java.io.Serializable;

import enums.Format;

public class ResearchPaper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String authors;
    private String journal;
    private int pages;

    public ResearchPaper(String title, String authors, String journal, int pages) {
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
    }

    public String getCitation(Format f) {
        if (f == Format.BIBTEX) {
            return "@article{" + title.replace(" ", "_") + ", author={" + authors + "}, journal={" + journal + "}}";
        }
        return authors + ". " + title + ". " + journal + ".";
    }

    @Override
    public String toString() {
        return "Paper: " + title + " (" + journal + ")";
    }
}

