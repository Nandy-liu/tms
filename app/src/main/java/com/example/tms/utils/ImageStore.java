package com.example.tms.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.tms.model.Api;
import com.example.tms.model.domain.PersonalResource;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageStore {
    public void update(Bitmap bitmap, SQLiteDatabase db, String id) {
        //bitmapToBytes
        byte[] by = bitmapToBytes(bitmap);
        ContentValues contentValues = new ContentValues();
        contentValues.put("person_img", by);
        db.update("personal_resource", contentValues, "id = ? ", new String[]{id});
    }

    public static Bitmap getBmp(SQLiteDatabase db, String id) {
        Cursor cursor = db.query("personal_resource", null, "id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            byte[] bytes = cursor.getBlob((int) cursor.getColumnIndex("person_img"));
            if (bytes != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            else
                return null;
        }
        return null;
    }

    public void getBmp(SQLiteDatabase db, String id, Handler myHandler) {
        Cursor cursor = db.query("personal_resource", null, "id = ?", new String[]{id}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                byte[] bytes = cursor.getBlob((int) cursor.getColumnIndex("person_img"));
                if (bytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                    Message msg = new Message();
                    msg.what = Constans.OK;
                    Bundle b = new Bundle();
                    b.putParcelable("img", bitmap);
                    msg.setData(b);
                    myHandler.sendMessage(msg);
                }
            }
        } else {
            Api api = HttpUtil.getInstance().getApi();
            Call<PersonalResource> task = api.RESOURCE_GET(id);
            task.enqueue(new Callback<PersonalResource>() {
                @Override
                public void onResponse(Call<PersonalResource> call, Response<PersonalResource> response) {
                    String img = response.body().getData().get(0).getPersonImg();
                    if (img != null) {
                        byte[] bytes = img.getBytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                        Message msg = new Message();
                        msg.what = Constans.OK;
                        Bundle b = new Bundle();
                        b.putParcelable("img", bitmap);
                        msg.setData(b);
                        myHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onFailure(Call<PersonalResource> call, Throwable t) {

                }
            });
        }
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        //除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }
}
