package org.androidtown.lbs.map;

import android.graphics.drawable.Drawable;

/**
 * Created by dksek_000 on 2016-11-27.
 */

public class ListItem {

    private Drawable iconDrawable ;
    private String titleStr ;
    private String content;


    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }

    public void setContent(String grade)
    {content = grade;}



    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getContent(){return this.content;}

}
