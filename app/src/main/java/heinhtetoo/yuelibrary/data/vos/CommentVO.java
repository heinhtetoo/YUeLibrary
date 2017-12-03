package heinhtetoo.yuelibrary.data.vos;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class CommentVO {

    private int commentId;

    private String commentWriter;

    private String commentBody;

    public CommentVO() {
    }

    public CommentVO(int commentId, String commentWriter, String commentBody) {
        this.commentId = commentId;
        this.commentWriter = commentWriter;
        this.commentBody = commentBody;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentWriter() {
        return commentWriter;
    }

    public String getCommentBody() {
        return commentBody;
    }
}
