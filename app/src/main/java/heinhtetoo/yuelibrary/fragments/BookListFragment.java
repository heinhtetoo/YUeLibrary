package heinhtetoo.yuelibrary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.adapters.BookListAdapter;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.data.models.BookModel;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.events.DataEvents;

public class BookListFragment extends BaseFragment {

    @Bind(R.id.rv_book_list)
    RecyclerView rvBookList;

    private View rootView;

    private BookListAdapter mBookListAdapter;
    private BookItemController mController;

    //private int mCategory;
    private List<BookVO> mBookList = new ArrayList<>();

    private BookModel mBookModel;

    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        return fragment;
    }

    public BookListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mController = (BookItemController) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookModel = BookModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.bind(this,  rootView);

        setupBookRecyclerView();

        mBookList = mBookModel.getBookList();
        displayBookList(mBookList, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mBookModel.loadBooks();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mBookListAdapter.clearData();
    }

    private void setupBookRecyclerView() {
        mBookListAdapter = new BookListAdapter(this.getContext(), mController);
        rvBookList.setLayoutManager(new GridLayoutManager(this.getContext(), 3, GridLayoutManager.VERTICAL, false));
        rvBookList.setAdapter(mBookListAdapter);
    }

    public boolean isBookListEmpty() {
        return mBookListAdapter == null || mBookListAdapter.getItemCount() == 0;
    }

    public void displayBookList(List<BookVO> bookList, boolean isToAppend) {
        if (isToAppend) {
            mBookListAdapter.appendNewData(bookList);
        } else {
            mBookListAdapter.setNewData(bookList);
        }
    }

    public void displayFailToLoadData(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnBookListLoaded(DataEvents.BookListLoadedEvent event) {
        mBookList = event.getBookList();
        mBookListAdapter.setNewData(mBookList);
    }
}
