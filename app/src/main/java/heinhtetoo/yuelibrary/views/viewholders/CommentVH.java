package heinhtetoo.yuelibrary.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.CommentItemController;
import heinhtetoo.yuelibrary.data.vos.CommentVO;

/**
 * Created by Hein Htet Oo on 12/5/2017.
 */

public class CommentVH extends BaseViewHolder<CommentVO> {

    @Bind(R.id.iv_profile)
    ImageView ivProfile;

    @Bind(R.id.tv_writer)
    TextView tvWriter;

    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_body)
    TextView tvBody;

    private CommentVO mComment;
    private CommentItemController mCommentItemController;

    public CommentVH(View itemView, CommentItemController mCommentItemController) {
        super(itemView);
        this.mCommentItemController = mCommentItemController;
    }

    @Override
    public void bind(CommentVO data) {
        mComment = data;
        Glide.with(ivProfile.getContext())
                .load(mComment.getCommentWriterProfileUrl())
                .placeholder(R.drawable.ic_account)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile);

        tvWriter.setText(mComment.getCommentWriter());
        tvTime.setText(String.valueOf(mComment.getCommentTimestamp()));
        tvBody.setText(mComment.getCommentBody());
    }

    @Override
    public void onClick(View view) {
        mCommentItemController.onClickComment(view, mComment);
    }
}