package com.spd.camera;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.spd.camera.widget.CameraPreview;

/**
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 *
 * @author :Reginer in  2017/5/26 9:58.
 *         联系方式:QQ:282921012
 *         功能描述:前后摄像头拍照
 */
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView mSvRear;
    private SurfaceView mSvFront;
    private Camera mCameraRear;
    private Camera mCameraFront;
    private CameraPreview mRearPreview;
    private CameraPreview mFrontPreview;
    public static final String TAG = "Reginer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        InitData();
    }

    private void initView() {
        mSvRear = (SurfaceView) findViewById(R.id.sv_rear);
        mSvFront = (SurfaceView) findViewById(R.id.sv_front);
        findViewById(R.id.btn_take_pic).setOnClickListener(this);
    }

    private void InitData() {
        mFrontPreview = new CameraPreview(this, mSvFront, true);
        mFrontPreview.setKeepScreenOn(true);
        mRearPreview = new CameraPreview(this, mSvRear, false);
        mRearPreview.setKeepScreenOn(true);
        mSvFront.setZOrderMediaOverlay(true);
        mSvFront.setZOrderOnTop(true);
        mFrontPreview.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mRearPreview.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_pic:
                takePicture();
                break;
        }
    }

    /**
     * 拍照.
     */
    private void takePicture() {
        try {
            mCameraFront.takePicture(null, null, picCallback);
            mCameraRear.takePicture(null, null, picCallback);
        } catch (Exception t) {
            t.printStackTrace();
            try {
                mCameraFront.startPreview();
                mCameraRear.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照回调.
     */
    private Camera.PictureCallback picCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //保存图片在这里写,data就是图片
            resetCamera();
        }
    };

    /**
     * 重置相机
     */
    private void resetCamera() {
        try {
            mCameraFront.startPreview();
            mFrontPreview.setCamera(mCameraFront);
            mCameraRear.startPreview();
            mRearPreview.setCamera(mCameraRear);
        } catch (Exception e) {
            resetCamera();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        releaseCamera();
        super.onPause();

    }

    private void releaseCamera() {
        if (mCameraFront != null) {
            mCameraFront.stopPreview();
            mFrontPreview.setCamera(null);
            mCameraFront.release();
            mCameraFront = null;
            mFrontPreview.setNull();
        }
        if (mCameraRear != null) {
            mCameraRear.stopPreview();
            mRearPreview.setCamera(null);
            mCameraRear.release();
            mCameraRear = null;
            mRearPreview.setNull();
        }

    }


    /**
     * 初始化相机.
     */
    private void setCamera() throws Exception {
        try {
            mCameraRear = Camera.open(0);
            mCameraRear.startPreview();
            mRearPreview.setCamera(mCameraRear);
            mRearPreview.reAutoFocus();
        } catch (RuntimeException ex) {
            Log.d(TAG, "没有后置相机 ");
        }
        try {
            mCameraFront = Camera.open(1);
            mCameraFront.startPreview();
            mFrontPreview.setCamera(mCameraFront);
            mFrontPreview.reAutoFocus();
        } catch (RuntimeException ex) {
            Log.d(TAG, "没有前置相机 ");
        }


    }

}
