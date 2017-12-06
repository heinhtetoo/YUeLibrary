package heinhtetoo.yuelibrary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import heinhtetoo.yuelibrary.adapters.LibBookListAdapter;
import heinhtetoo.yuelibrary.controllers.LibBookItemController;
import heinhtetoo.yuelibrary.data.models.LibBookModel;
import heinhtetoo.yuelibrary.data.vos.LibBookVO;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public class LibBookListFragment extends BaseFragment {

    @Bind(R.id.rv_lib_book_list)
    RecyclerView rvLibBookList;

    private View rootView;

    private LibBookListAdapter mLibBookListAdapter;
    private LibBookItemController mController;

    //private int mCategory;
    private List<LibBookVO> mLibBookList = new ArrayList<>();

    private LibBookModel mLibBookModel;

    public static LibBookListFragment newInstance() {
        LibBookListFragment fragment = new LibBookListFragment();
        return fragment;
    }

    public LibBookListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mController = (LibBookItemController) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLibBookModel = LibBookModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lib_book_list, container, false);
        ButterKnife.bind(this, rootView);

        setupLibBookRecyclerView();

        mLibBookList = mLibBookModel.getLibBookList();
        displayLibBookList(mLibBookList, false);

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
        mLibBookModel.loadLibBooks();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mLibBookListAdapter.clearData();
    }

    private void setupLibBookRecyclerView() {
        mLibBookListAdapter = new LibBookListAdapter(this.getContext(), mController);
        rvLibBookList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        rvLibBookList.setAdapter(mLibBookListAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvLibBookList.addItemDecoration(decoration);
    }

    public boolean isLibBookListEmpty() {
        return mLibBookListAdapter == null || mLibBookListAdapter.getItemCount() == 0;
    }

    public void displayLibBookList(List<LibBookVO> libBookList, boolean isToAppend) {
        if (isToAppend) {
            mLibBookListAdapter.appendNewData(libBookList);
        } else {
            mLibBookListAdapter.setNewData(libBookList);
        }
    }

    public void displayFailToLoadData(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnBookListLoaded(DataEvents.LibBookListLoadedEvent event) {
        mLibBookList = event.getLibBookList();
        mLibBookListAdapter.setNewData(mLibBookList);
    }
}
