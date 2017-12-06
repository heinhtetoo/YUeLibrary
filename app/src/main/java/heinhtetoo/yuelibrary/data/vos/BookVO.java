package heinhtetoo.yuelibrary.data.vos;

import java.util.List;

/**
 * Created by Hein Htet Oo on 11/21/2017.
 */

public class BookVO {

    private int id;

    private String name;

    private String author;

    private String cover_art;

    private List<String> category;

    private String language;

    private int pageCount;

    private String description;

    private String download_url;

    public BookVO() {
    }

    public BookVO(int id, String name, String author, String cover_art, List<String> category, String language, int pageCount, String description, String download_url) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.cover_art = cover_art;
        this.category = category;
        this.language = language;
        this.pageCount = pageCount;
        this.description = description;
        this.download_url = download_url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCover_art() {
        return cover_art;
    }

    public List<String> getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getDescription() {
        return description;
    }

    public String getDownload_url() {
        return download_url;
    }
}
