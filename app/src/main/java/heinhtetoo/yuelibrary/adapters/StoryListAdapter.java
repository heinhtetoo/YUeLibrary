package heinhtetoo.yuelibrary.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.StoryItemController;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.views.viewholders.StoryVH;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryListAdapter extends BaseRecyclerAdapter<StoryVH, StoryVO> {

    private StoryItemController mStoryItemController;

    public StoryListAdapter(Context context, StoryItemController mStoryItemController) {
        super(context);
        this.mStoryItemController = mStoryItemController;
    }

    @Override
    public StoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_story, parent, false);
        return new StoryVH(view, mStoryItemController);
    }

    @Override
    public void onBindViewHolder(StoryVH holder, int position) {
        holder.bind(mData.get(position));
    }
}