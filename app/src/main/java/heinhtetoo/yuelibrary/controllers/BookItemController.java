package heinhtetoo.yuelibrary.controllers;

import android.view.View;

import heinhtetoo.yuelibrary.data.vos.BookVO;

/**
 * Created by Hein Htet Oo on 11/21/2017.
 */

public interface BookItemController extends BaseController {
    void onClickBook(View view, BookVO book);
}
