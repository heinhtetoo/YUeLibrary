package heinhtetoo.yuelibrary.data.vos;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public class LibBookVO {

    private int id;

    private String name;

    private String author;

    private String cover_art;

    private List<String> category;

    private String language;

    private int pageCount;

    private String description;

    private boolean available;

    private String reservedUserId;

    public LibBookVO() {
    }

    public LibBookVO(int id, String name, String author, String cover_art, List<String> category, String language, int pageCount, String description, boolean available, String reservedUserId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.cover_art = cover_art;
        this.category = category;
        this.language = language;
        this.pageCount = pageCount;
        this.description = description;
        this.available = available;
        this.reservedUserId = reservedUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(String reservedUserId) {
        this.reservedUserId = reservedUserId;
    }
}
