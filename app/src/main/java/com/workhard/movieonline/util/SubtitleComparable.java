package com.workhard.movieonline.util;

import com.workhard.movieonline.model.Subtitle;

import java.util.Comparator;

/**
 * Created by TrungKD on 2/26/2017.
 */
public class SubtitleComparable implements Comparator<Subtitle> {
    @Override
    public int compare(Subtitle subtitle1, Subtitle subtitle2) {
        String language1 = subtitle1.getLanguage();
        String language2 = subtitle2.getLanguage();
        return language1.compareTo(language2);
    }
}
