package heinhtetoo.yuelibrary.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.CommentItemController;
import heinhtetoo.yuelibrary.data.vos.CommentVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

/**
 * Created by Hein Htet Oo on 12/5/2017.
 */

public class CommentVH extends BaseViewHolder<CommentVO> {

    @Bind(R.id.iv_profile)
    CircleImageView ivProfile;

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
                .dontAnimate()
                .placeholder(R.drawable.ic_account)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile);

        String writer = MMFontUtils.mmTextUnicodeOrigin(mComment.getCommentWriter());
        String comment = MMFontUtils.mmTextUnicodeOrigin(mComment.getCommentBody());

        //TODO : show commented time
        tvWriter.setText(writer);
        tvTime.setText(String.valueOf(mComment.getCommentTimestamp()));
        tvTime.setVisibility(View.GONE);
        tvBody.setText(comment);
    }

    @Override
    public void onClick(View view) {
        mCommentItemController.onClickComment(view, mComment);
    }
}