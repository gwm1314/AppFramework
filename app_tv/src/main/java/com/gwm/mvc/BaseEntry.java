package com.gwm.mvc;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * MVC模式中Model层的基类
 * @author gwm
 * public class MyEntry extends BaseEntry {
     public static final Parcelable.Creator<MyEntry> CREATOR
             = new Parcelable.Creator<MyEntry>() {
         public MyEntry createFromParcel(Parcel in) {
             return new MyEntry(in);
         }
         public MyEntry[] newArray(int size) {
             return new MyEntry[size];
         }
     };
}
 */
public abstract class BaseEntry implements Parcelable{
    public BaseEntry(){}
    protected BaseEntry(Parcel in) {
        readByParcel(in);
    }
    public int describeContents() {
        return 0;
    }
    public abstract void writeToParcel(Parcel out, int flags);
    public abstract void readByParcel(Parcel in);
}