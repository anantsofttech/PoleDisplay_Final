package com.poledisplayapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import poledisplayapp.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.demono.adapter.InfinitePagerAdapter;
import com.poledisplayapp.Constant;
import com.poledisplayapp.MainActivity;
import com.poledisplayapp.models.ImagesDetailModel;

import java.io.File;
import java.util.List;

public class MyCustomPagerAdapter extends InfinitePagerAdapter {

    Context context;
    boolean isRefresh = false;
    //    int images[];

    List<ImagesDetailModel> imageDetalList;
    //    private String imgUrl = Constant.IMG_BASE + Constant.IMG_BANNER_URL +Constant.STOREID+"/";
    private String imgUrlpath = Constant.IMG_BASE + Constant.IMG_BANNER_URL;

    public MyCustomPagerAdapter(Context context, List<ImagesDetailModel> imageDetalList, boolean isRefresh) {
        this.context = context;
        this.imageDetalList = imageDetalList;
        this.isRefresh = isRefresh;
    }

    @Override
    public int getItemCount() {
        if (imageDetalList == null) {
            return 0;
        } else if (this.imageDetalList.size() > 0) {
            return this.imageDetalList.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public View getItemView(int position, View convertView, ViewGroup container) {
        {
            View itemView = null;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.rawpager, container, false);
            ImagesDetailModel model = imageDetalList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imgbanner);
            if (model.getImage().contains("dummy1")) {
                imageView.setImageResource(R.drawable.adv1);
            } else if (model.getImage().contains("dummy2")) {
                imageView.setImageResource(R.drawable.adv2);
            } else if (model.getImage().contains("dummy3")) {
                imageView.setImageResource(R.drawable.adv3);
            }else if (model.getImage().contains("no image")){
                imageView.setImageResource(R.drawable.noproductimageavailable);
            }
//            else if (Constant.isFromBlipBoard) {
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        String url = model.getImage();
//                        String url = model.getImagepath().toString();
//                        Glide.with(context)
//                                .load(url)
//                                .timeout(1000000)
//                                .fitCenter()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .override(800, 1280)
//                                .priority(Priority.IMMEDIATE)
//                                .into(imageView);
//
//                        //                                .error(R.drawable.adv1)
//                    }
//                });
//
////            } else if (model.getImage().contains("/pos/")) {
//            }
            else if (model.getImagepath().toString().contains("/pos/")) {
                imageView.setImageURI(Uri.parse(model.getImagepath().toString()));
                File file = new File(Uri.parse(model.getImagepath().toString()).getPath());
                //to prevent to display dummy images ****
                if (file.length() <= 0) {
//                    imageView.setImageResource(R.drawable.adv1);
                    imageView.setImageResource(R.drawable.loader_test);
                }
                //end ******
            }
            else {

//                old working code start _______________
//                // commented below line for displaying centralized img with imgpath
////                String url = imgUrlpath + model.getStoreNo() + "/" + model.getImage();
//                String url = model.getImagepath().toString();
//                ///end**********
//                Glide.with(imageView.getContext())
//                        .load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
////                        .error(R.drawable.adv1)
//                        .error(R.drawable.loader_test)
//                        .override(800, 1280)
//                        .dontAnimate()
//                        .dontTransform()
//                        .fitCenter()
//                        .into(imageView);
//                end ________________

                // commented below line for displaying centralized img with imgpath
//                String url = imgUrlpath + model.getStoreNo() + "/" + model.getImage();
                String url = model.getImagepath().toString();
                ///end**********

                if(!url.isEmpty() && !url.equalsIgnoreCase("")) {

                    Glide.with(imageView.getContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.adv1)
//                            .error(R.drawable.loader_test)
                            .override(800, 1280)
                            .dontAnimate()
                            .dontTransform()
                            .fitCenter()
                            .into(imageView);
                }
            }
            if (position == 0) {
                //Log.d("kaveriImage","ZERO");

//                if (!Constant.isFromBlipBoard)
                    MainActivity.dismissProgressBar();

                if (Constant.loading != null && Constant.loading.isShowing()) {
                    Constant.loading.dismiss();
                    Constant.loading = null;
                }

            }
            return itemView;
        }
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

}
