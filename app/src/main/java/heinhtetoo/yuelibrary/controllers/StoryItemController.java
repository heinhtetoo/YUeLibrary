package heinhtetoo.yuelibrary.controllers;

import android.view.View;

import heinhtetoo.yuelibrary.data.vos.StoryVO;

/**
 * Created by Hein Htet Oo on 12/1/2017.
 */

public interface StoryItemController {
    void onClickStory(View view, StoryVO story);
}
