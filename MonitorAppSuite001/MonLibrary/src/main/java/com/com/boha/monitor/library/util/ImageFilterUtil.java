package com.com.boha.monitor.library.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import com.boha.monitor.library.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by aubreyM on 2014/06/21.
 */
public class ImageFilterUtil implements GLSurfaceView.Renderer {

    public ImageFilterUtil (Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (mCurrentEffect != R.id.none) {
            //if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        renderResult();
    }
    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */
        if (mCurrentEffect == R.id.none) {
        } else if (mCurrentEffect == R.id.autofix) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_AUTOFIX);
            mEffect.setParameter("scale", 0.5f);

        } else if (mCurrentEffect == R.id.bw) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_BLACKWHITE);
            mEffect.setParameter("black", .1f);
            mEffect.setParameter("white", .7f);

        } else if (mCurrentEffect == R.id.brightness) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_BRIGHTNESS);
            mEffect.setParameter("brightness", 2.0f);

        } else if (mCurrentEffect == R.id.contrast) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_CONTRAST);
            mEffect.setParameter("contrast", 1.4f);

        } else if (mCurrentEffect == R.id.crossprocess) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_CROSSPROCESS);

        } else if (mCurrentEffect == R.id.documentary) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_DOCUMENTARY);

        } else if (mCurrentEffect == R.id.duotone) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_DUOTONE);
            mEffect.setParameter("first_color", Color.YELLOW);
            mEffect.setParameter("second_color", Color.DKGRAY);

        } else if (mCurrentEffect == R.id.filllight) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FILLLIGHT);
            mEffect.setParameter("strength", .8f);

        } else if (mCurrentEffect == R.id.fisheye) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FISHEYE);
            mEffect.setParameter("scale", .5f);

        } else if (mCurrentEffect == R.id.flipvert) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("vertical", true);

        } else if (mCurrentEffect == R.id.fliphor) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("horizontal", true);

        } else if (mCurrentEffect == R.id.grain) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_GRAIN);
            mEffect.setParameter("strength", 1.0f);

        } else if (mCurrentEffect == R.id.grayscale) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_GRAYSCALE);

        } else if (mCurrentEffect == R.id.lomoish) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_LOMOISH);

        } else if (mCurrentEffect == R.id.negative) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_NEGATIVE);

        } else if (mCurrentEffect == R.id.posterize) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_POSTERIZE);

        } else if (mCurrentEffect == R.id.rotate) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_ROTATE);
            mEffect.setParameter("angle", 180);

        } else if (mCurrentEffect == R.id.saturate) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SATURATE);
            mEffect.setParameter("scale", .5f);

        } else if (mCurrentEffect == R.id.sepia) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SEPIA);

        } else if (mCurrentEffect == R.id.sharpen) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SHARPEN);

        } else if (mCurrentEffect == R.id.temperature) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_TEMPERATURE);
            mEffect.setParameter("scale", .9f);

        } else if (mCurrentEffect == R.id.tint) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_TINT);
            mEffect.setParameter("tint", Color.MAGENTA);

        } else if (mCurrentEffect == R.id.vignette) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_VIGNETTE);
            mEffect.setParameter("scale", .5f);

        } else {
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (mCurrentEffect != R.id.none) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }
    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);


        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();
    }
    private Bitmap bitmap;
    private GLSurfaceView mEffectView;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    int mCurrentEffect;
}
