package heinhtetoo.yuelibrary.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.CommentItemController;
import heinhtetoo.yuelibrary.data.vos.CommentVO;
import heinhtetoo.yuelibrary.views.viewholders.CommentVH;

/**
 * Created by Hein Htet Oo on 12/5/2017.
 */

public class CommentListAdapter extends BaseRecyclerAdapter<CommentVH, CommentVO> {

    private CommentItemController mCommentItemController;

    public CommentListAdapter(Context context, CommentItemController mCommentItemController) {
        super(context);
        this.mCommentItemController = mCommentItemController;
    }

    @Override
    public CommentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentVH(view, mCommentItemController);
    }

    @Override
    public void onBindViewHolder(CommentVH holder, int position) {
        holder.bind(mData.get(position));
    }
}