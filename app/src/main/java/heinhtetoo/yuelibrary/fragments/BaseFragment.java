package heinhtetoo.yuelibrary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Hein Htet Oo on 11/30/2017.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            readArguments(bundle);
        }
    }

    protected void readArguments(Bundle bundle) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
