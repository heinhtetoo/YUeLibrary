package heinhtetoo.yuelibrary.fragments;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.activities.HomeActivity;
import heinhtetoo.yuelibrary.adapters.HomePagerAdapter;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 11/30/2017.
 */

public class HomePagerFragment extends Fragment {

    @Bind(R.id.pager_home)
    ViewPager pagerHome;

    @Bind(R.id.tl_home)
    TabLayout tlHome;

    private HomePagerAdapter mHomePagerAdapter;

    private int[] tabIcons = {
            R.drawable.ic_shelf,
            R.drawable.ic_story
    };

    public static HomePagerFragment newInstance() {
        return new HomePagerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHomePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());
        mHomePagerAdapter.addTab(BookListFragment.newInstance(), getString(R.string.shelf));
        mHomePagerAdapter.addTab(StoryListFragment.newInstance(), getString(R.string.story));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_pager, container, false);
        ButterKnife.bind(this, rootView);

        pagerHome.setAdapter(mHomePagerAdapter);
        pagerHome.setOffscreenPageLimit(mHomePagerAdapter.getCount());
        tlHome.setupWithViewPager(pagerHome);

        setupTabIcons();

        tlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String title = "";

                switch (tlHome.getSelectedTabPosition()) {
                    case 0:
                        title = getString(R.string.shelf);
                        break;
                    case 1:
                        title = getString(R.string.story);
                        break;
                    default:
                        title = getString(R.string.shelf);
                        break;
                }

                DataEvents.TabLayoutChangedEvent event = new DataEvents.TabLayoutChangedEvent(title);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    private void setupTabIcons() {

        TextView tabShelf = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        tabShelf.setText(getString(R.string.shelf));
        tabShelf.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_shelf, 0, 0);
        tlHome.getTabAt(0).setCustomView(tabShelf);

        TextView tabStory = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        tabStory.setText(getString(R.string.story));
        tabStory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_story, 0, 0);
        tlHome.getTabAt(1).setCustomView(tabStory);
    }
}
