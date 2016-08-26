package crixec.app.imagefactory.util.bootimage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Constant;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.XmlDataUtils;

/**
 * Created by Crixec on 2016/8/13.
 */
public class DeviceGetter extends AsyncTask<Void, Void, Device> {
    private Context context;
    private EditText editText;
    private ProgressDialog dialog;
    private int which;

    public DeviceGetter(Context context, EditText editText, int which) {
        this.context = context;
        this.editText = editText;
        this.which = which;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(R.style.ProgressBar);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.getting_device_information));
        dialog.show();
    }

    @Override
    protected void onPostExecute(Device device) {
        super.onPostExecute(device);
        dialog.dismiss();
        if (device.getRecoveryPath().equals("") || device.getKernelPath().equals("")) {
            Toast.makeShortText(context.getString(R.string.maybe_this_function_unsupported_your_device));
        } else {
            if (which == Device.KERNEL)
                editText.setText(device.getKernelPath());
            else
                editText.setText(device.getRecoveryPath());
        }
    }

    @Override
    protected Device doInBackground(Void... params) {
        Device device = null;
        String kernel = XmlDataUtils.getString(Constant.KEY_KERNEL_PATH);
        String recovery = XmlDataUtils.getString(Constant.KEY_RECOVERY_PATH);
        if (kernel.equals("(null)") || recovery.equals("(null)")) {
            device = new Device("", "");
        } else if (kernel.equals("") || recovery.equals("")) {
            device = DeviceManager.get();
            if (device.getKernelPath().equals("") || device.getRecoveryPath().equals("")) {
                kernel = "(null)";
                recovery = "(null)";
            } else {
                kernel = device.getKernelPath();
                recovery = device.getRecoveryPath();
            }
            XmlDataUtils.putString(Constant.KEY_KERNEL_PATH, kernel);
            XmlDataUtils.putString(Constant.KEY_RECOVERY_PATH, recovery);
        } else {
            device = new Device(recovery, kernel);
        }
        return device;
    }
}
