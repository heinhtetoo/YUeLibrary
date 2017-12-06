package heinhtetoo.yuelibrary.controllers;

import android.view.View;

import heinhtetoo.yuelibrary.data.vos.CommentVO;

/**
 * Created by Hein Htet Oo on 12/5/2017.
 */

public interface CommentItemController {
    void onClickComment(View view, CommentVO comment);
}
