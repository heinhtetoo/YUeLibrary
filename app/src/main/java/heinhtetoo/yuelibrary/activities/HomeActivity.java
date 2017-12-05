package heinhtetoo.yuelibrary.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.adapters.BookListAdapter;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.controllers.StoryItemController;
import heinhtetoo.yuelibrary.data.models.UserModel;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.events.DataEvents;
import heinhtetoo.yuelibrary.fragments.HomePagerFragment;

public class HomeActivity extends AppCompatActivity implements BookItemController, StoryItemController {

    private static final int SHELF = 100;
    private static final int STORY = 200;
    private static final String CURRENT_INDEX = "CURRENT_INDEX";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    /*@Bind(R.id.rv_book_list)
    RecyclerView mRecycler;*/

    /*public BookListAdapter mBookListAdapter;
    public ArrayList<BookVO> bookVOS;*/

    private int mCurrentIndex;

    private DatabaseReference mUserDr;

    @Override
    protected void onStart() {
        super.onStart();

        String userId = UserModel.getInstance().getAccountIdFromPref(this);
        mUserDr = FirebaseDatabase.getInstance().getReference().child(UserModel.LIB_USER);
        if (userId != null) {
            UserModel.getInstance().loadUserFromPref(userId);
        }

        EventBus.getDefault().register(this);
        //BookModel.getInstance().loadBooks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);
        //bookVOS = new ArrayList<>();

        if (savedInstanceState == null) {
            showHome();
            setTitle(getString(R.string.shelf));
            mCurrentIndex = SHELF;
        }

        /*bookVOS.add(new BookVO("A Song of Ice and Fire", "George R. R. Martin", "https://upload.wikimedia.org/wikipedia/en/thumb/d/dc/A_Song_of_Ice_and_Fire_book_collection_box_set_cover.jpg/220px-A_Song_of_Ice_and_Fire_book_collection_box_set_cover.jpg"));
        bookVOS.add(new BookVO("Harry Potter and the Deathly Hallows", "J. K. Rowling", "http://t3.gstatic.com/images?q=tbn:ANd9GcR7z2hnrPlPSgnWd1W2W923Xftc5Ascp-FxCLmtYieKv5zSnJ6r"));
        bookVOS.add(new BookVO("သူငယ်ချင်းလို့ပဲဆက်၍ခေါ်မည် ခိုင်", "တက္ကသိုလ် ဘုန်းနိုင်", "http://www.freemyanmarbook.com/img/cover/COB6392_1.jpg"));*/

        //setupBookRecyclerView();
    }

    /*private void setupBookRecyclerView() {
        mBookListAdapter = new BookListAdapter(getApplicationContext(), this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mBookListAdapter);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX);

        switch (mCurrentIndex) {
            case SHELF:
                tvTitle.setText(R.string.shelf);
                break;
            case STORY:
                tvTitle.setText(R.string.story);
                break;
        }
    }

    @OnClick(R.id.fab_new_story)
    public void onClickFabNewStory() {
        if (UserModel.getInstance().isUserSignIn()) {
            startAddStoryActivity();
        } else {
            Snackbar.make(tvTitle, "You need to sign in with Google to add story.", Snackbar.LENGTH_INDEFINITE).setAction("Sign In", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startUserAccountActivity();
                }
            }).show();
        }
    }

    @Override
    public void onClickBook(View view, BookVO book) {
        Intent intent = BookDetailActivity.newIntent(this, book.getId());

        startActivity(intent);
    }

    @Override
    public void onClickBook(View view, StoryVO story) {
        Intent intent = StoryDetailActivity.newIntent(this, story.getStoryId());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_account) {
            startUserAccountActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHome() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_pager, HomePagerFragment.newInstance())
                .commit();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    private void startAddStoryActivity() {
        Intent intent = AddStoryActivity.newIntent(this);

        startActivity(intent);
    }

    private void startUserAccountActivity() {
        Intent intent = UserAccountActivity.newIntent(this);

        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnBookListLoaded(DataEvents.TabLayoutChangedEvent event) {
        setTitle(event.getTitle());
    }
}
