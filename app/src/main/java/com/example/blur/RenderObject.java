package com.example.blur;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RenderObject {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;
    private Rect myRect = new Rect();
    private int programId;
    private int aPositionHandle;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;
    private int widthOfsetHandle;
    private int heightOfsetHandle;
    private int gaussianWeightsHandle;
    private int blurRadiusHandle;
    private int scaleWidth,scaleHeight;
    public RenderObject(){
        final float[] vertexData = {
                1f, -1f, 0f,
                -1f, -1f, 0f,
                1f, 1f, 0f,
                -1f, 1f, 0f
        };


        final float[] textureVertexData = {
                1f, 0f,
                0f, 0f,
                1f, 1f,
                0f, 1f
        };
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(textureVertexData);
        textureVertexBuffer.position(0);

    }


    public void initShader(Context context){
        String vertexShader = ShaderUtils.readRawTextFile(context, R.raw.vertext_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(context, R.raw.fragment_sharder);
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
        widthOfsetHandle = GLES20.glGetUniformLocation(programId, "widthOfset");
        heightOfsetHandle = GLES20.glGetUniformLocation(programId, "heightOfset");
        gaussianWeightsHandle = GLES20.glGetUniformLocation(programId, "gaussianWeights");
        blurRadiusHandle = GLES20.glGetUniformLocation(programId, "blurRadius");
    }

    public void setScaleSize(int width,int height){
        scaleWidth = width;
        scaleHeight = height;
    }


    /**
     * 不必太纠结  做适配的
     * @param screenWidth
     * @param screenHeight
     */
    public void setScreenSize(int screenWidth,int screenHeight){
        float sh = screenWidth * 1.0f / screenHeight;
        float vh = scaleWidth * 1.0f / scaleHeight;
        int left, top, viewWidth, viewHeight;
        if (sh < vh) {
            left = 0;
            viewWidth = screenWidth;
            viewHeight = (int) (scaleHeight * 1.0f / scaleWidth * viewWidth);
            top = (screenHeight - viewHeight) / 2;
        } else {
            top = 0;
            viewHeight = screenHeight;
            viewWidth = (int) (scaleWidth * 1.0f / scaleHeight * viewHeight);
            left = (screenWidth - viewWidth) / 2;
        }
        myRect.left = left;
        myRect.top = top;
        myRect.right = left + viewWidth;
        myRect.bottom = top + viewHeight;
    }

    private FloatBuffer weightsBuffer;
    public void gaussianWeights(){
        if(blurRadius == 0){
            return;
        }
        float sumOfWeights = 0.0f;
        int g = 0;
        int tx = blurRadius*2+1;
        if(sigma == 0){
            sigma = 0.3f*((tx-1)*0.5f - 1f) + 0.8f;
        }
        float gaussianWeights[] = new float[tx*tx];
        for (int x = -blurRadius; x <= blurRadius; x++) {
            for (int y = -blurRadius; y <= blurRadius; y++) {
                int s = x*x+y*y;
                float a = (float) ((1.0f / 2.0f * Math.PI * Math.pow(sigma, 2.0f)) *
                        Math.exp(-s / (2.0f * Math.pow(sigma, 2.0f))));
                gaussianWeights[g] = a;
                sumOfWeights+=a;
                g++;
            }
        }
        for (int x = 0; x < tx*tx; ++x) {
            gaussianWeights[x] = gaussianWeights[x]/sumOfWeights;
        }
        weightsBuffer = ByteBuffer.allocateDirect(gaussianWeights.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(gaussianWeights);
        weightsBuffer.position(0);
    }
    private int blurRadius = 2;
    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    private double sigma = 3;
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void drawFrame(int texture){
        GLES20.glViewport(myRect.left, myRect.top, myRect.width(), myRect.height());
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
//      纹理
        GLES20.glUniform1i(uTextureSamplerHandle, 0);

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);

        GLES20.glUniform1f(widthOfsetHandle, 1.0f/scaleWidth);
        GLES20.glUniform1f(heightOfsetHandle, 1.0f/scaleHeight);
        GLES20.glUniform1i(blurRadiusHandle, blurRadius);
        int tx = blurRadius*2+1;
        GLES20.glUniform1fv(gaussianWeightsHandle,tx*tx, weightsBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
