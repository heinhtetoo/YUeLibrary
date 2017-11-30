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

    private String downloadUrl;

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

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
