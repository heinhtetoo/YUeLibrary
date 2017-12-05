package heinhtetoo.yuelibrary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import heinhtetoo.yuelibrary.adapters.StoryListAdapter;
import heinhtetoo.yuelibrary.controllers.StoryItemController;
import heinhtetoo.yuelibrary.data.models.StoryModel;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.events.DataEvents;

public class StoryListFragment extends BaseFragment {

    @Bind(R.id.rv_story_list)
    RecyclerView rvStoryList;

    private View rootView;

    private StoryListAdapter mStoryListAdapter;
    private StoryItemController mController;

    //private int mCategory;
    private List<StoryVO> mStoryList = new ArrayList<>();

    private StoryModel mStoryModel;

    public static StoryListFragment newInstance() {
        StoryListFragment fragment = new StoryListFragment();
        return fragment;
    }

    public StoryListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mController = (StoryItemController) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStoryModel = StoryModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_story_list, container, false);
        ButterKnife.bind(this, rootView);

        setupBookRecyclerView();

        mStoryList = mStoryModel.getStoryList();
        displayStoryList(mStoryList, false);

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
        mStoryModel.loadStories();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mStoryListAdapter.clearData();
    }

    private void setupBookRecyclerView() {
        mStoryListAdapter = new StoryListAdapter(this.getContext(), mController);
        rvStoryList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        rvStoryList.setAdapter(mStoryListAdapter);
    }

    public boolean isStoryListEmpty() {
        return mStoryListAdapter == null || mStoryListAdapter.getItemCount() == 0;
    }

    public void displayStoryList(List<StoryVO> storyList, boolean isToAppend) {
        if (isToAppend) {
            mStoryListAdapter.appendNewData(storyList);
        } else {
            mStoryListAdapter.setNewData(storyList);
        }
    }

    public void displayFailToLoadData(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnStoryListLoaded(DataEvents.StoryListLoadedEvent event) {
        mStoryList = event.getStoryList();
        mStoryListAdapter.setNewData(mStoryList);
    }
}
