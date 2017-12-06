package heinhtetoo.yuelibrary.views.viewholders;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.StoryItemController;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryVH extends BaseViewHolder<StoryVO> {

    @Bind(R.id.iv_author_profile)
    CircleImageView ivAuthorProfile;

    @Bind(R.id.tv_author)
    TextView tvAuthor;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.iv_story_pic)
    ImageView ivStoryPic;

    @Bind(R.id.tv_tags)
    TextView tvTags;

    private StoryVO mStory;
    private StoryItemController mStoryItemController;

    public StoryVH(View itemView, StoryItemController mStoryItemController) {
        super(itemView);
        this.mStoryItemController = mStoryItemController;
    }

    @Override
    public void bind(StoryVO data) {
        mStory = data;
        Glide.with(ivAuthorProfile.getContext())
                .load(mStory.getAuthorProfileUrl())
                .placeholder(R.drawable.ic_account)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAuthorProfile);

        if (mStory.getPictureUrl() != null && !mStory.getPictureUrl().isEmpty()) {
            Glide.with(ivStoryPic.getContext())
                    .load(mStory.getPictureUrl())
                    .placeholder(R.drawable.manga_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStoryPic);
        }

        //TODO : show time and views count

        tvAuthor.setText(MMFontUtils.mmTextUnicodeOrigin(mStory.getAuthorName()));
        tvTitle.setText(MMFontUtils.mmTextUnicodeOrigin(mStory.getTitle()));

        if (0 < mStory.getTags().size()) {
            tvTags.setText(mStory.getTags().get(0));
            tvTags.setVisibility(View.VISIBLE);
        }
        else {
            tvTags.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        mStoryItemController.onClickStory(view, mStory);
    }
}
