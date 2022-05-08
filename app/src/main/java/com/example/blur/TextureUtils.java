package com.example.blur;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureUtils {
    public int texture(Bitmap mBitmap) {
        int textures[] = new int[1];
        //可以生成多个纹理
        GLES20.glGenTextures(textures.length, textures, 0);
        //绑定纹理  一般绑定之后， 下面的操作， 对绑定的纹理起作用  状态机
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        //缩小怎么搞
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        //放大怎么搞
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //边缘处理
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        //讲图片资源写入
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        return textures[0];
    }
}
