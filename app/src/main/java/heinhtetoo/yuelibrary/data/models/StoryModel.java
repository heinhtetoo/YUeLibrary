package heinhtetoo.yuelibrary.data.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryModel {

    private static final String STORY = "stories";

    private static StoryModel objInstance;

    public List<StoryVO> storyList;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mStoryDr;

    public StoryModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        storyList = new ArrayList<>();
    }

    public static StoryModel getInstance() {
        if (objInstance == null) {
            objInstance = new StoryModel();
        }
        return objInstance;
    }

    public void loadStories() {
        mStoryDr = mDatabaseReference.child(STORY);
        final List<StoryVO> storyVOS = new ArrayList<>();
        mStoryDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        StoryVO story = snapshot.getValue(StoryVO.class);
                        storyVOS.add(story);
                    }

                    Collections.sort(storyList, new Comparator<StoryVO>() {
                        @Override
                        public int compare(StoryVO s1, StoryVO s2) {
                            if (s1.getStoryId() > s2.getStoryId())
                                return 1;
                            else if (s1.getStoryId() < s2.getStoryId())
                                return -1;

                            return 0;
                        }
                    });

                    DataEvents.StoryListLoadedEvent event = new DataEvents.StoryListLoadedEvent(storyVOS);
                    EventBus.getDefault().post(event);

                    storyList = storyVOS;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<StoryVO> getStoryList() {
        if (storyList == null) {
            storyList = new ArrayList<>();
        }
        return storyList;
    }

    public StoryVO getStoryById(int id) {
        StoryVO story = new StoryVO();
        for (StoryVO storyVO : storyList) {
            if (storyVO.getStoryId() == id) {
                story = storyVO;
            }
        }
        return story;
    }

}
