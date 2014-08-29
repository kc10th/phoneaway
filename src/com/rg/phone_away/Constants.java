
package com.rg.phone_away;

import java.io.IOException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public abstract class Constants {

    public static final String finishDownloadIconAction = "com.rg.phone_away.FINISHLOADICON";

    public static final String punishVoiceFinish = "com.rg.phone_away.PUNISHVOICEFINISH";

    public abstract class PackageName {

        public static final String packageName = "com.rg.phone_away"; // app 包名

        public static final String contactsPackageName = "com.android.contacts"; // 联系人界面包名

        public static final String phonePackageName = "com.android.phone"; // 通话界面

        public static final String mmsPackageName = "com.android.mms"; // 短信界面包名

        public static final String settingsPackageName = "com.android.settings"; // 短信界面包名

    }

    public static int getPunishPicture(int num) {
        final int[] picture = {
                R.drawable.punish_picture_0, R.drawable.punish_picture_1,
                R.drawable.punish_picture_2, R.drawable.punish_picture_3,
                R.drawable.punish_picture_4, R.drawable.punish_picture_5,
                R.drawable.punish_picture_6, R.drawable.punish_picture_7,
                R.drawable.punish_picture_8, R.drawable.punish_picture_9,
                R.drawable.punish_picture_10
        };

        if (num == -1) {
            int random = (int)(System.currentTimeMillis() % 10);
            return picture[random];
        }
        return picture[num % 10];
    }

    public static Bitmap getLockPicture(MyApp app) {

        String scene = null;
        String pattern = null;

        if (MyApp.Scene.equals(MyApp.SceneStrings[0])) {
            scene = "study";
        }
        if (MyApp.Scene.equals(MyApp.SceneStrings[1])) {
            scene = "work";
        }
        if (MyApp.Scene.equals(MyApp.SceneStrings[2])) {
            scene = "party";
        }
        if (MyApp.Pattern.equals(MyApp.PatternStrings[0])) {
            pattern = "stimulate";
        }
        if (MyApp.Pattern.equals(MyApp.PatternStrings[1])) {
            pattern = "strike";
        }

        AssetManager manager = app.getAssets();
        String fileName = "warning_picture/" + scene + "_" + pattern + "/" + "warning_word_"
                + scene + "_" + pattern + "_" + System.currentTimeMillis() % 10 + ".png";
        Log.i("com.rg.phone_away.Constants.getLockPicture", "asset/" + fileName);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(manager.open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    static final int[] award_picture_boys = {
            R.drawable.award_picture_boy_0, R.drawable.award_picture_boy_1,
            R.drawable.award_picture_boy_2, R.drawable.award_picture_boy_3,
            R.drawable.award_picture_boy_4
    };

    static final int[] award_picture_girls = {
            R.drawable.award_picture_girl_0, R.drawable.award_picture_girl_1,
            R.drawable.award_picture_girl_2, R.drawable.award_picture_girl_3,
            R.drawable.award_picture_girl_4
    };

    static int getAwardPicture() {
        int[] award_picture;
        if (MyApp.mainActivity.myAccessToken.sex.equals("m")) {
            award_picture = award_picture_boys;
        } else {
            award_picture = award_picture_girls;
        }
        return award_picture[(int)(System.currentTimeMillis() % 5)];
    }

}
