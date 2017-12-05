package heinhtetoo.yuelibrary.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.data.models.StoryModel;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

public class AddStoryActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.et_title)
    EditText etTitle;

    @Bind(R.id.cl_added_image)
    CoordinatorLayout clAddedImage;

    @Bind(R.id.iv_added_photo)
    ImageView ivAddedPhoto;

    @Bind(R.id.et_body)
    EditText etBody;

    @Bind(R.id.et_tags)
    EditText etTags;

    private String mPhotoUrl = "";

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddStoryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_story_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_done:
                onClickAddStory();
                return true;

            case R.id.action_add_photo:
                onClickAddPhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            onPhotoTaken(uri.toString());
        }
    }

    public void onClickAddStory() {
        final String storyTitle = etTitle.getText().toString();
        final String storyBody = etBody.getText().toString();
        final String[] storyTags = etTags.getText().toString().split(" ");
        final List<String> tagList = new ArrayList<>();

        tagList.addAll(Arrays.asList(storyTags));

        if (TextUtils.isEmpty(storyTitle)) {
            etTitle.setError("Need a title for your story.");
        } else if (TextUtils.isEmpty(storyBody)) {
            etBody.setError("Write a story to publish");
        } else if (tagList.size() <= 0) {
            etBody.setError("Tag a category of your story.");
        } else {
            showProgressDialogInfinite("Posting your story");
            if (mPhotoUrl.isEmpty()) {
                dismissProgressDialog();
                StoryModel.getInstance().addStory(storyTitle, storyBody, tagList, null);
                onBackPressed();
                StoryModel.getInstance().loadStories();
            } else {
                StoryModel.getInstance().uploadFile(mPhotoUrl, new StoryModel.UploadFileCallback() {
                    @Override
                    public void onUploadSucceeded(String uploadedPaths) {
                        StoryModel.getInstance().addStory(storyTitle, storyBody, tagList, uploadedPaths);
                        dismissProgressDialog();
                        onBackPressed();
                        StoryModel.getInstance().loadStories();
                    }

                    @Override
                    public void onUploadFailed(String msg) {
                        Snackbar.make(etTags, "Your photo couldn't be uploaded because : " + msg, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void onClickAddPhoto() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select a Photo for your Story"), PICK_IMAGE_REQUEST);
    }

    private void onPhotoTaken(String photoUrl) {
        if (TextUtils.isEmpty(photoUrl)) {
            Snackbar.make(etTags, "ERROR : Path to photo is empty.", Snackbar.LENGTH_LONG).show();
        } else {
            setImageView(photoUrl);
            clAddedImage.setVisibility(View.VISIBLE);
        }
    }

    public void setImageView(String photoUrl) {
        mPhotoUrl = photoUrl;
        Glide.with(this.getApplicationContext())
                .load(photoUrl)
                .centerCrop()
                .placeholder(R.drawable.manga_image)
                .error(R.drawable.manga_image)
                .into(ivAddedPhoto);
    }

    private void showProgressDialogInfinite(String msg) {
        mProgressDialog = new ProgressDialog(this);

        if (!MMFontUtils.isSupportUnicode()) {
            mProgressDialog.setMessage(Html.fromHtml(MMFontUtils.uni2zg(msg)));
        } else {
            mProgressDialog.setMessage(Html.fromHtml(msg));
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
