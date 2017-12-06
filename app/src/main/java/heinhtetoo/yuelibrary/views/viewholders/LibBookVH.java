package heinhtetoo.yuelibrary.views.viewholders;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.controllers.LibBookItemController;
import heinhtetoo.yuelibrary.data.vos.LibBookVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

/**
 * Created by Hein Htet Oo on 12/6/2017.
 */

public class LibBookVH extends BaseViewHolder<LibBookVO> {

    @Bind(R.id.iv_lib_cover)
    ImageView ivCover;

    @Bind(R.id.tv_lib_book_title)
    TextView tvTitle;

    @Bind(R.id.tv_available)
    TextView tvAvailable;

    @Bind(R.id.btn_reserve)
    Button btnReserve;

    private LibBookVO mLibBook;
    private LibBookItemController mLibBookItemController;

    public LibBookVH(View itemView, LibBookItemController mLibBookItemController) {
        super(itemView);
        this.mLibBookItemController = mLibBookItemController;
    }

    @Override
    public void bind(LibBookVO data) {
        mLibBook = data;
        Glide.with(ivCover.getContext())
                .load(mLibBook.getCover_art())
                .placeholder(R.drawable.manga_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover);

        tvTitle.setText(MMFontUtils.mmTextUnicodeOrigin(mLibBook.getName()));
        if (mLibBook.isAvailable()) {
            tvAvailable.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.book_available));
            tvAvailable.setText(itemView.getContext().getString(R.string.book_available));
            btnReserve.setEnabled(true);
            btnReserve.setClickable(true);
        } else {
            tvAvailable.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.book_reserved));
            tvAvailable.setText(itemView.getContext().getString(R.string.book_reserved));
            btnReserve.setEnabled(false);
            btnReserve.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        mLibBookItemController.onClickLibBook(view, mLibBook);
    }

    @OnClick(R.id.btn_reserve)
    public void onClickBtnReserve(View view) {
        mLibBookItemController.onClickReserveBook(view, mLibBook);
    }
}