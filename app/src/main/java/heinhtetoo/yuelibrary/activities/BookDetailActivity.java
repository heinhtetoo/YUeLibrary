package heinhtetoo.yuelibrary.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.data.vos.BookVO;

public class BookDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.iv_book_cover)
    ImageView ivCover;

    @Bind(R.id.tv_name)
    TextView tvTitle;

    @Bind(R.id.tv_author)
    TextView tvAuthor;

    @Bind(R.id.tv_categories)
    TextView tvCategories;

    @Bind(R.id.tv_description)
    TextView tvDescription;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private String bookId;
    private String bookName;
    private String bookCover;

    public static Intent newIntent(Context context, String id, String name, String cover) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("bookId", id);
        intent.putExtra("bookName", name);
        intent.putExtra("bookCover", cover);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this, this);

        bookId = getIntent().getStringExtra("bookId");
        bookName = getIntent().getStringExtra("bookName");
        bookCover = getIntent().getStringExtra("bookCover");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bindData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindData() {
        Glide.with(ivCover.getContext())
                .load(bookCover)
                .placeholder(R.drawable.manga_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover);
        tvTitle.setText(bookName);
        tvAuthor.setText("Someone");
        tvCategories.setText("Romance");
        tvDescription.setText("Nothing");
    }
}
