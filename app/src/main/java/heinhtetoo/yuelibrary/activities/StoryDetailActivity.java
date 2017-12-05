package heinhtetoo.yuelibrary.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.adapters.CommentListAdapter;
import heinhtetoo.yuelibrary.controllers.CommentItemController;
import heinhtetoo.yuelibrary.data.models.StoryModel;
import heinhtetoo.yuelibrary.data.models.UserModel;
import heinhtetoo.yuelibrary.data.vos.CommentVO;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

public class StoryDetailActivity extends AppCompatActivity implements CommentItemController {

    private static final String STORY = "stories";
    private static final String COMMENT_LIST = "commentList";
    private static final String IE_STORY_ID = "storyId";

    @Bind(R.id.iv_detail_author_profile)
    ImageView ivAuthorProfile;

    @Bind(R.id.tv_detail_author)
    TextView tvAuthor;

    @Bind(R.id.tv_detail_published_date)
    TextView tvDate;

    @Bind(R.id.tv_detail_react_count)
    TextView tvReactCount;

    @Bind(R.id.tv_detail_view_count)
    TextView tvViewCount;

    @Bind(R.id.tv_detail_title)
    TextView tvTitle;

    @Bind(R.id.iv_detail_story_pic)
    ImageView ivStoryPic;

    @Bind(R.id.tv_detail_body)
    TextView tvBody;

    @Bind(R.id.rv_detail_comment_list)
    RecyclerView rvCommentList;

    @Bind(R.id.et_comment)
    EditText etComment;

    @Bind(R.id.layout_detail_tags)
    LinearLayout layoutTags;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mCommentDr;

    private StoryVO mStory;

    private CommentListAdapter mCommentListAdapter;

    private int storyId;

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, StoryDetailActivity.class);
        intent.putExtra(IE_STORY_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        ButterKnife.bind(this, this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        storyId = getIntent().getIntExtra(IE_STORY_ID, 0);
        mStory = StoryModel.getInstance().getStoryById(storyId);

        mCommentDr = mDatabaseReference.child(STORY).child(String.valueOf(storyId)).child(COMMENT_LIST);

        setupBookRecyclerView();

        bindData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.iv_send_comment)
    public void onClickSendComment() {
        final String commentBody = etComment.getText().toString();

        if (!TextUtils.isEmpty(commentBody)) {
            if (UserModel.getInstance().isUserSignIn()) {
                addComment(commentBody);
            } else {
                Snackbar.make(tvTitle, "You need to sign in with Google to comment.", Snackbar.LENGTH_INDEFINITE).setAction("Sign In", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(UserAccountActivity.newIntent(getApplicationContext()));
                    }
                }).show();
            }
        }
    }

    private void setupBookRecyclerView() {
        mCommentListAdapter = new CommentListAdapter(getApplicationContext(), this);
        rvCommentList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCommentList.setAdapter(mCommentListAdapter);
    }

    public boolean isStoryListEmpty() {
        return mCommentListAdapter == null || mCommentListAdapter.getItemCount() == 0;
    }

    public void displayCommentList(List<CommentVO> commentList, boolean isToAppend) {
        if (isToAppend) {
            mCommentListAdapter.appendNewData(commentList);
        } else {
            mCommentListAdapter.setNewData(commentList);
        }
    }

    public void displayFailToLoadData(String message) {
        Snackbar.make(rvCommentList, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    private void bindData() {
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
            TextView textView = new TextView(this);
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
        tvViewCount.setText(String.valueOf(mStory.getViewList().size()));
        tvTitle.setText(mStory.getTitle());
        tvBody.setText(mStory.getBody());

        displayCommentList(mStory.getCommentList(), false);
    }

    public void addComment(String commentBody) {
        List<CommentVO> newCommentList;
        newCommentList = mStory.getCommentList();
        CommentVO comment = CommentVO.initComment(commentBody);
        newCommentList.add(comment);
        mCommentDr.setValue(newCommentList);
        displayCommentList(newCommentList, false);
        etComment.setText("");
    }

    @Override
    public void onClickComment(View view, CommentVO comment) {
        //Toast.makeText(this, "Comment Clicked", Toast.LENGTH_SHORT).show();
    }
}
