package models;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import enums.Format;

public class ResearchPaper implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String authors;
    private String journal;
    private int pages;
    private int citationsCount;
    private Date datePublished;

    public ResearchPaper(String title, String authors, String journal, int pages) {
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
        this.citationsCount = 0;
        this.datePublished = new Date();
    }


    public String getCitation(Format f) {
        if (f == Format.BIBTEX) {
            return String.format("@article{%s, author={%s}, journal={%s}, year={%tY}, pages={%d}}", 
                title.replace(" ", "_"), authors, journal, datePublished, pages);
        }
        return String.format("%s. \"%s\". %s, %tY. pp. %d. Citations: %d", 
                authors, title, journal, datePublished, pages, citationsCount);
    }


    public void addCitation() {
        this.citationsCount++;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(datePublished);
    }


    public int getPages() { return pages; }
    public Date getDatePublished() { return datePublished; }
    public int getCitationsCount() { return citationsCount; }
    public String getTitle() { return title; }
    
    public void setCitationsCount(int count) { this.citationsCount = count; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %d pages, %d citations", 
                getFormattedDate(), title, journal, pages, citationsCount);
    }
}

