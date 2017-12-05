package heinhtetoo.yuelibrary.data.vos;

import heinhtetoo.yuelibrary.data.models.UserModel;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class CommentVO {

    private int commentId;

    private String commentWriterId;

    private String commentWriter;

    private String commentWriterProfileUrl;

    private String commentBody;

    private long commentTimestamp;

    public CommentVO() {
    }

    public CommentVO(int commentId, String commentWriterId, String commentWriter, String commentWriterProfileUrl, String commentBody, long commentTimestamp) {
        this.commentId = commentId;
        this.commentWriterId = commentWriterId;
        this.commentWriter = commentWriter;
        this.commentWriterProfileUrl = commentWriterProfileUrl;
        this.commentBody = commentBody;
        this.commentTimestamp = commentTimestamp;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentWriterId() {
        return commentWriterId;
    }

    public String getCommentWriter() {
        return commentWriter;
    }

    public String getCommentWriterProfileUrl() {
        return commentWriterProfileUrl;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public long getCommentTimestamp() {
        return commentTimestamp;
    }

    public static CommentVO initComment(String cmt) {
        CommentVO comment = new CommentVO();
        comment.commentBody = cmt;

        comment.commentId = (int) (System.currentTimeMillis() / 1000);
        comment.commentTimestamp = System.currentTimeMillis() / 1000;

        comment.commentWriterId = UserModel.getInstance().getUser().getAccountId();
        comment.commentWriter = UserModel.getInstance().getUser().getDisplayName();
        comment.commentWriterProfileUrl = UserModel.getInstance().getUser().getPhotoUrl();

        return comment;
    }
}
