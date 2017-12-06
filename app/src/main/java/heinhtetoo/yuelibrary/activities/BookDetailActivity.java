package heinhtetoo.yuelibrary.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.data.models.BookModel;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

public class BookDetailActivity extends AppCompatActivity {

    private static final String IE_BOOK_ID = "bookId";

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

    @Bind(R.id.tv_page_count)
    TextView tvPageCount;

    @Bind(R.id.tv_language)
    TextView tvLanguage;

    private BookVO mBook;

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(IE_BOOK_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this, this);

        int bookId = getIntent().getIntExtra(IE_BOOK_ID, 0);
        mBook = BookModel.getInstance().getBookById(bookId);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bindData();

    }

    @OnClick(R.id.btn_read)
    public void onClickBtnRead(View view) {
        Intent intent = PdfViewActivity.newIntent(view.getContext(), mBook.getName(), mBook.getDownload_url());
        startActivity(intent);
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

        String name = "unavailable";
        String author = "unavailable";
        String cover_art = "";
        String categoryStr = "";
        String description = "unavailable";
        String language = "unknown";

        if (mBook.getCategory().size() > 0) {
            List<String> categoryArray = mBook.getCategory();
            for (int i = 0; i < categoryArray.size(); i++) {
                categoryStr += categoryArray.get(i) + "| ";
            }
            categoryStr = categoryStr.trim();
            categoryStr = categoryStr.substring(0, categoryStr.length() - 1);
        } else {
            categoryStr = "unavailable";
        }

        if (mBook.getName() != null && !mBook.getName().isEmpty()) {
            name = mBook.getName();
        }

        if (mBook.getAuthor() != null && !mBook.getAuthor().isEmpty()) {
            author = mBook.getAuthor();
        }

        if (mBook.getCover_art() != null && !mBook.getCover_art().isEmpty()) {
            cover_art = mBook.getCover_art();
        }

        if (mBook.getDescription() != null && !mBook.getDescription().isEmpty()) {
            description = mBook.getDescription();
        }

        if (mBook.getLanguage() != null && !mBook.getLanguage().isEmpty()) {
            language = mBook.getLanguage();
        }

        name = MMFontUtils.mmTextUnicodeOrigin(name);
        author = MMFontUtils.mmTextUnicodeOrigin(author);
        description = MMFontUtils.mmTextUnicodeOrigin(description);
        language = MMFontUtils.mmTextUnicodeOrigin(language);

        Glide.with(ivCover.getContext())
                .load(cover_art)
                .placeholder(R.drawable.manga_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover);
        tvTitle.setText(name);
        tvAuthor.setText(author);
        tvCategories.setText(categoryStr);
        tvDescription.setText(description);
        tvPageCount.setText(mBook.getPageCount() + " pages");
        tvLanguage.setText(language);
    }
}
