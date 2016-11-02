/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.janardhan.blood2life.utils;

import android.content.Context;
import android.widget.ImageView;

import com.janardhan.blood2life.R;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class GlideUtil {


    public static void loadProfileIcon(String url, ImageView imageView) {
        Context context = imageView.getContext();


        Picasso.with(context)
                .load(url) //extract as User instance method
                .transform(new CropCircleTransformation())
                .resize(56, 56)
                .into(imageView);

        /*Glide.with(context)
                .load(url)
                .placeholder(R.drawable.place_holder)
                .dontAnimate()
                .fitCenter()
                .into(imageView);*/
    }
}