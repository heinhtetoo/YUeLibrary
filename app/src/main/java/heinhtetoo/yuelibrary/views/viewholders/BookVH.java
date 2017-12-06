package heinhtetoo.yuelibrary.views.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.BookItemController;
import heinhtetoo.yuelibrary.data.vos.BookVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

/**
 * Created by Hein Htet Oo on 11/21/2017.
 */

public class BookVH extends BaseViewHolder<BookVO> {

    @Bind(R.id.iv_cover)
    ImageView ivCover;

    @Bind(R.id.tv_name)
    TextView tvName;

    private BookVO mBook;
    private BookItemController mBookItemController;

    public BookVH(View itemView, BookItemController mBookItemController) {
        super(itemView);
        this.mBookItemController = mBookItemController;
    }

    @Override
    public void bind(BookVO data) {
        mBook = data;
        Glide.with(ivCover.getContext())
                .load(mBook.getCover_art())
                .placeholder(R.drawable.manga_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover);

        tvName.setText(MMFontUtils.mmTextUnicodeOrigin(mBook.getName()));
    }

    @Override
    public void onClick(View view) {
        mBookItemController.onClickBook(view, mBook);
    }
}
