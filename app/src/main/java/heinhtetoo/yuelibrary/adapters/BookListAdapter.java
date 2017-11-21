package heinhtetoo.yuelibrary.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.views.viewholders.BookVH;

/**
 * Created by Hein Htet Oo on 11/21/2017.
 */

public class BookListAdapter extends BaseRecyclerAdapter<BookVH, BookVO> {

    private BookItemController mBookItemController;

    public BookListAdapter(Context context, BookItemController mBookItemController) {
        super(context);
        this.mBookItemController = mBookItemController;
    }

    @Override
    public BookVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_book, parent, false);
        return new BookVH(view, mBookItemController);
    }

    @Override
    public void onBindViewHolder(BookVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
