package com.example.blur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRender implements GLSurfaceView.Renderer {
    public RenderObject renderObject;//渲染
    private FrameBufferObject frameBufferObject; //fbo
    private Bitmap bitmap;
    private Context context;
    public MyRender(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        renderObject = new RenderObject();
        renderObject.initShader(context);
        frameBufferObject = new FrameBufferObject();
        frameBufferObject.initShader();
        bitmap = fileBitmap(context.getResources(),R.drawable.b);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        renderObject.setScaleSize(200,200);
        renderObject.gaussianWeights();
        renderObject.setScreenSize(i,i1);
    }

    public void change(){
//        renderObject.setScaleSize(200,200);
        renderObject.gaussianWeights();
    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1,1,1,1);
        GLES20.glClear(GLES20.GL_COLOR_CLEAR_VALUE|GLES20.GL_COLOR_BUFFER_BIT);
        frameBufferObject.drawFrame(bitmap,1000,1000);
        renderObject.drawFrame(frameBufferObject.getTexture());
    }

    public Bitmap fileBitmap(Resources res, int id) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,id,opts);
        opts.inSampleSize = calculateInSampleSize(opts,500,500);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,id,opts);
    }


    public int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                     int reqHeight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;

        int inSampleSize = 1;
        if(originalWidth > originalHeight){
            int halfWidth = originalWidth;
            while (halfWidth > reqWidth) {
                inSampleSize *= 2;
                halfWidth = halfWidth/2;
            }
        }else{
            int halfHeight = originalHeight;
            while (halfHeight  > reqHeight) {
                inSampleSize *= 2;
                halfHeight = halfHeight/2;
            }
        }
        return inSampleSize;
    }


}
