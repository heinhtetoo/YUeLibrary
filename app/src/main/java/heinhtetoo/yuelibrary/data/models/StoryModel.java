package heinhtetoo.yuelibrary.data.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import heinhtetoo.yuelibrary.data.vos.CommentVO;
import heinhtetoo.yuelibrary.data.vos.StoryVO;
import heinhtetoo.yuelibrary.events.DataEvents;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public class StoryModel {

    private static final String STORY = "stories";
    public static final String FIREBASE_STORAGE_BUCKET = "gs://yu-elibrary.appspot.com";
    public static final String UPLOAD_IMAGE_PATH = "images";

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

                    Collections.sort(storyVOS, new Comparator<StoryVO>() {
                        @Override
                        public int compare(StoryVO s1, StoryVO s2) {
                            if (s1.getStoryId() > s2.getStoryId())
                                return -1;
                            else if (s1.getStoryId() < s2.getStoryId())
                                return 1;
                            else
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

    public int addStory(String title, String body, List<String> tags, String image) {
        StoryVO story = StoryVO.initStory(title, body, tags, image);
        mStoryDr.child(String.valueOf(story.getPublishedDate())).setValue(story);
        return story.getStoryId();
    }

    public void uploadFile(String fileToUpload, final UploadFileCallback callback) {
        Uri file = Uri.parse(fileToUpload);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE_BUCKET);

        StorageReference uploadFileRef = storageRef.child(UPLOAD_IMAGE_PATH + "/" + file.getLastPathSegment());
        UploadTask uploadTask = uploadFileRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onUploadFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uploadedImageUrl = taskSnapshot.getDownloadUrl();
                Log.d("Add New Story", "Uploaded Image Url : " + uploadedImageUrl);

                callback.onUploadSucceeded(uploadedImageUrl.toString());
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

    public interface UploadFileCallback {
        void onUploadSucceeded(String uploadedPaths);

        void onUploadFailed(String msg);
    }

}
