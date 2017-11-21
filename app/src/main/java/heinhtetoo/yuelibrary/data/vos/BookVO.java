package heinhtetoo.yuelibrary.data.vos;

/**
 * Created by Hein Htet Oo on 11/21/2017.
 */

public class BookVO {

    private String name;

    private String author;

    private String cover_art;

    public BookVO(String name, String author, String cover_art) {
        this.name = name;
        this.author = author;
        this.cover_art = cover_art;
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
}
