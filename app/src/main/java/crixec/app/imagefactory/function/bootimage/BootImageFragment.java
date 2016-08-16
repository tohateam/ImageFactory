package crixec.app.imagefactory.function.bootimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BackupBootImageActivity;
import crixec.app.imagefactory.activity.FlashBootImageActivity;
import crixec.app.imagefactory.activity.PortBootImageActivity;
import crixec.app.imagefactory.activity.RepackBootImageActivity;
import crixec.app.imagefactory.activity.UnpackBootImageActivity;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;

/**
 * Created by crixec on 2016/8/2.
 */
public class BootImageFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.boot_image);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        Intent intent = null;
        String key = preference.getKey();
        if (cmp(key, R.string.operate_unpack_boot_image_key)) {
            intent = new Intent(getContext(), UnpackBootImageActivity.class);
        } else if (cmp(key, R.string.operate_repack_boot_image_key)) {
            intent = new Intent(getContext(), RepackBootImageActivity.class);
        } else if (cmp(key, R.string.flash_boot_image_key)) {
            intent = new Intent(getContext(), FlashBootImageActivity.class);
        } else if (cmp(key, R.string.backup_boot_image_key)) {
            intent = new Intent(getContext(), BackupBootImageActivity.class);
        } else if (cmp(key, R.string.port_boot_image_key)) {
            intent = new Intent(getContext(), PortBootImageActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preference);
    }

    public boolean cmp(String key, int stringId) {
        return key.equals(getString(stringId));
    }
}
