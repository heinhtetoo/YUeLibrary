package heinhtetoo.yuelibrary.data.vos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryVO {

    private int storyId;

    private String authorId;

    private String authorName;

    private String authorProfileUrl;

    private String publishedDate;

    private String title;

    private String pictureUrl;

    private String body;

    private List<String> reactList;

    private List<String> viewList;

    private List<CommentVO> commentList;

    private List<String> tags;

    public StoryVO() {
    }

    public StoryVO(int storyId, String authorId, String authorName, String authorProfileUrl, String publishedDate, String title, String pictureUrl, String body, List<String> reactList, List<String> viewList, List<CommentVO> commentList, List<String> tags) {
        this.storyId = storyId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorProfileUrl = authorProfileUrl;
        this.publishedDate = publishedDate;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.body = body;
        this.reactList = reactList;
        this.viewList = viewList;
        this.commentList = commentList;
        this.tags = tags;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorProfileUrl() {
        return authorProfileUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getBody() {
        return body;
    }

    public List<String> getReactList() {
        if (reactList == null) {
            reactList = new ArrayList<>();
        }
        return reactList;
    }

    public List<String> getViewList() {
        if (viewList == null) {
            viewList = new ArrayList<>();
        }
        return viewList;
    }

    public List<CommentVO> getCommentList() {
        if (commentList == null) {
            commentList = new ArrayList<>();
        }
        return commentList;
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }
}
