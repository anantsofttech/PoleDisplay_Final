package com.poledisplayapp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import poledisplayapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.poledisplayapp.models.ImagesDetailModel;

public class AutoScrollPagerFragment extends Fragment {

    ImageView imgbanner;
    public static ImagesDetailModel model1;
    // commented below line for displaying centralized img with imgpath
    private String imgUrl = Constant.IMG_BASE + Constant.IMG_BANNER_URL +Constant.STOREID+"/";
//    private String imgNoImageUrl = Constant.IMG_BASE + Constant.IMG_NO_IMAGE + "/";
    public static AutoScrollPagerFragment newInstance(ImagesDetailModel model) {
        model1 = model;

        return new AutoScrollPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rawpager, container, false);
        imgbanner=view.findViewById(R.id.imgbanner);
        if(model1 != null){
           //Log.e("Log","IMG URL="+imgUrl + model1.getImage());
            // Bitmap bitmap = Utils.textAsBitmap("Technical Problem", 28);
            //Drawable d = new BitmapDrawable(getActivity().getResources(), bitmap);
            Glide.with(getActivity()).load(imgUrl + model1.getImage())
                    /* .placeholder(R.drawable.progress_bar)*/
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imgbanner);
        }
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    /*if (isRecyclerRootViewAlways()) {
      mRootView = null;//<--
    }mMyFragmentLifecycle.onFragmentDestroyView(this);*/
    }

}
