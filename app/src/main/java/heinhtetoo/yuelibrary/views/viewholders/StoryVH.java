package heinhtetoo.yuelibrary.views.viewholders;

import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import butterknife.Bind;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.StoryItemController;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryVH extends BaseViewHolder<StoryVO> {

    @Bind(R.id.iv_author_profile)
    ImageView ivAuthorProfile;

    @Bind(R.id.tv_author)
    TextView tvAuthor;

    @Bind(R.id.tv_published_date)
    TextView tvDate;

    @Bind(R.id.tv_react_count)
    TextView tvReactCount;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.iv_story_pic)
    ImageView ivStoryPic;

    @Bind(R.id.tv_body)
    TextView tvBody;

    @Bind(R.id.layout_tags)
    LinearLayout layoutTags;

    @Bind(R.id.tv_attributes_session)
    TextView tvAttributesSession;

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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAuthorProfile);

        if (mStory.getPictureUrl() != null && !mStory.getPictureUrl().isEmpty()) {
            Glide.with(ivStoryPic.getContext())
                    .load(mStory.getPictureUrl())
                    .placeholder(R.drawable.manga_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStoryPic);
        } else {
            ivStoryPic.setVisibility(View.GONE);
        }

        layoutTags.removeAllViews();

        for (String tag : mStory.getTags()) {
            TextView textView = new TextView(itemView.getContext());
            MMFontUtils.setMMFont(textView);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.color.primary_dark);
            textView.setGravity(Gravity.CENTER);
            if (!MMFontUtils.isSupportUnicode()) {
                textView.setText(Html.fromHtml(MMFontUtils.mmText(tag, MMFontUtils.TEXT_UNICODE, true, true)));
            } else {
                textView.setText(Html.fromHtml(tag));
            }
            textView.setPadding(16, 16, 16, 16);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            layoutTags.addView(textView);
        }

        tvAuthor.setText(mStory.getAuthorName());
        tvDate.setText(mStory.getPublishedDate());
        tvReactCount.setText(String.valueOf(mStory.getReactList().size()));
        tvTitle.setText(mStory.getTitle());
        tvBody.setText(mStory.getBody());

        String comments = String.valueOf(mStory.getCommentList().size());
        String views = String.valueOf(mStory.getViewList().size());
        tvAttributesSession.setText(comments + "Comments     " + "|     " + views + "Views");
    }

    @Override
    public void onClick(View view) {
        mStoryItemController.onClickBook(view, mStory);
    }
}
