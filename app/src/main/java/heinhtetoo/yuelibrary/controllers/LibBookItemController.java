package heinhtetoo.yuelibrary.controllers;

import android.view.View;

import heinhtetoo.yuelibrary.data.vos.LibBookVO;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public interface LibBookItemController extends BaseController {
    void onClickLibBook(View view, LibBookVO libBook);

    void onClickReserveBook(View view, LibBookVO libBook);
}
