package com.workhard.movieonline.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.workhard.movieonline.R;

/**
 * Created by TrungKD on 3/7/2017.
 */
public class ImageUtil {
    public static void loadImage(Context context, String imageUrl, int targetWidth, int targetHeight, ImageView imageView, int idErrorImage) {
        Picasso.with(context).load(imageUrl).resize(targetWidth, targetHeight)
                .error(idErrorImage)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // logger.d("loading image url success");
                    }

                    @Override
                    public void onError() {
                        // logger.d("loading image url fail");
                    }
                });
    }
}
