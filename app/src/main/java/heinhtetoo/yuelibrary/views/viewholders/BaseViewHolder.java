package heinhtetoo.yuelibrary.views.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by aung on 6/13/17.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    public abstract void bind(T data);
}
