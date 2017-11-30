package heinhtetoo.yuelibrary.activities;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.adapters.BookListAdapter;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.data.models.BookModel;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.events.DataEvents;

public class HomeActivity extends AppCompatActivity implements BookItemController {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rv_book_list)
    RecyclerView mRecycler;

    public BookListAdapter mBookListAdapter;
    public ArrayList<BookVO> bookVOS;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        BookModel.getInstance().loadBooks();
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
        bookVOS = new ArrayList<>();

        /*bookVOS.add(new BookVO("A Song of Ice and Fire", "George R. R. Martin", "https://upload.wikimedia.org/wikipedia/en/thumb/d/dc/A_Song_of_Ice_and_Fire_book_collection_box_set_cover.jpg/220px-A_Song_of_Ice_and_Fire_book_collection_box_set_cover.jpg"));
        bookVOS.add(new BookVO("Harry Potter and the Deathly Hallows", "J. K. Rowling", "http://t3.gstatic.com/images?q=tbn:ANd9GcR7z2hnrPlPSgnWd1W2W923Xftc5Ascp-FxCLmtYieKv5zSnJ6r"));
        bookVOS.add(new BookVO("သူငယ်ချင်းလို့ပဲဆက်၍ခေါ်မည် ခိုင်", "တက္ကသိုလ် ဘုန်းနိုင်", "http://www.freemyanmarbook.com/img/cover/COB6392_1.jpg"));*/

        setupBookRecyclerView();
    }

    private void setupBookRecyclerView() {
        mBookListAdapter = new BookListAdapter(getApplicationContext(), this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mBookListAdapter);
    }

    @Override
    public void onClickBook(View view, BookVO book) {
        Intent intent = BookDetailActivity.newIntent(this.getApplicationContext(), book.getId());

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
            Intent intent = new Intent(this, UserAccountActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnBookListLoaded(DataEvents.BookListLoadedEvent event) {
        bookVOS = (ArrayList<BookVO>) event.getBookList();
        mBookListAdapter.setNewData(bookVOS);
    }
}
