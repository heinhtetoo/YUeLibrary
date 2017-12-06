package heinhtetoo.yuelibrary.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.controllers.LibBookItemController;
import heinhtetoo.yuelibrary.data.vos.LibBookVO;
import heinhtetoo.yuelibrary.views.viewholders.LibBookVH;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public class LibBookListAdapter extends BaseRecyclerAdapter<LibBookVH, LibBookVO> {

    private LibBookItemController mLibBookItemController;

    public LibBookListAdapter(Context context, LibBookItemController mLibBookItemController) {
        super(context);
        this.mLibBookItemController = mLibBookItemController;
    }

    @Override
    public LibBookVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_lib_book, parent, false);
        return new LibBookVH(view, mLibBookItemController);
    }

    @Override
    public void onBindViewHolder(LibBookVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
