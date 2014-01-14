package com.BeeFramework.theme;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.BeeFramework.example.R;
import com.external.activeandroid.util.Log;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */
public class BeeBitmapDrawable extends BeeDrawable
{
    private static final int[] attrArray = {android.R.attr.src ,android.R.attr.antialias, android.R.attr.filter, android.R.attr.dither,android.R.attr.gravity, android.R.attr.tileMode};

    private static final int DEFAULT_PAINT_FLAGS =
            Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;
    private BeeBitmapState mBitmapState;
    private Bitmap mBitmap;
    private int mTargetDensity;

    private final Rect mDstRect = new Rect();   // Gravity.apply() sets this

    private boolean mApplyGravity;
    private boolean mMutated;

    // These are scaled to match the target density.
    private int mBitmapWidth;
    private int mBitmapHeight;

    // Mirroring matrix for using with Shaders
    private Matrix mMirrorMatrix;

    public BeeBitmapDrawable(Bitmap bitmap) {
        this(new BeeBitmapState(bitmap), null);
    }

    public BeeBitmapDrawable(Resources res,Bitmap bitmap)
    {
        this(new BeeBitmapState(bitmap),res);
        mBitmapState.mTargetDensity = mTargetDensity;
    }

    private BeeBitmapDrawable(BeeBitmapState state, Resources res) {
        mBitmapState = state;
        if (res != null) {
            mTargetDensity = res.getDisplayMetrics().densityDpi;
        } else {
            mTargetDensity = state.mTargetDensity;
        }
        setBitmap(state != null ? state.mBitmap : null);
    }

    public BeeBitmapDrawable(String filepath)
    {
        this(new BeeBitmapState(BitmapFactory.decodeFile(filepath)),null);
        if (mBitmap == null)
        {

        }
    }

    public BeeBitmapDrawable(Resources res) {
        mBitmapState = new BeeBitmapState((Bitmap) null);
        mBitmapState.mTargetDensity = mTargetDensity;
    }

    private void setBitmap(Bitmap bitmap) {
        if (bitmap != mBitmap) {
            mBitmap = bitmap;
            if (bitmap != null) {
                computeBitmapSize();
            } else {
                mBitmapWidth = mBitmapHeight = -1;
            }
            invalidateSelf();
        }
    }

    private void computeBitmapSize() {
        mBitmapWidth = mBitmap.getScaledWidth(mTargetDensity);
        mBitmapHeight = mBitmap.getScaledHeight(mTargetDensity);
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (mTargetDensity != density) {
            mTargetDensity = density == 0 ? DisplayMetrics.DENSITY_DEFAULT : density;
            if (mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    private boolean needMirroring() {
        return false ;
    }

    private void updateMirrorMatrix(float dx) {
        if (mMirrorMatrix == null) {
            mMirrorMatrix = new Matrix();
        }
        mMirrorMatrix.setTranslate(dx, 0);
        mMirrorMatrix.preScale(-1.0f, 1.0f);
    }

    public void setGravity(int gravity) {
        if (mBitmapState.mGravity != gravity) {
            mBitmapState.mGravity = gravity;
            mApplyGravity = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean mipMap) {
        if (mBitmapState.mBitmap != null) {
            mBitmapState.mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    public void setAntiAlias(boolean aa) {
        mBitmapState.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean filter) {
        mBitmapState.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public void setDither(boolean dither) {
        mBitmapState.mPaint.setDither(dither);
        invalidateSelf();
    }

    public void setTileModeX(Shader.TileMode mode) {
        setTileModeXY(mode, mBitmapState.mTileModeY);
    }

    public final void setTileModeY(Shader.TileMode mode) {
        setTileModeXY(mBitmapState.mTileModeX, mode);
    }

    public void setTileModeXY(Shader.TileMode xmode, Shader.TileMode ymode) {
        final BeeBitmapState state = mBitmapState;
        if (state.mTileModeX != xmode || state.mTileModeY != ymode) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
            state.mRebuildShader = true;
            invalidateSelf();
        }
    }


    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mApplyGravity = true;
        Shader shader = mBitmapState.mPaint.getShader();
        if (shader != null)
        {
            if (mMirrorMatrix != null)
            {
                mMirrorMatrix = null;
                shader.setLocalMatrix(new Matrix());
            }
        }
    }



    @Override
    public void draw(Canvas canvas)
    {
        Bitmap bitmap = mBitmap;

        if (bitmap != null)
        {
            final BeeBitmapState state = mBitmapState;
            if (state.mRebuildShader)
            {
                Shader.TileMode tmx = state.mTileModeX;
                Shader.TileMode tmy = state.mTileModeY;

                if (tmx == null && tmy == null)
                {
                    state.mPaint.setShader(null);
                }
                else
                {
                    state.mPaint.setShader(new BitmapShader(bitmap,
                                        tmx == null ? Shader.TileMode.CLAMP :tmx,
                                        tmx == null ? Shader.TileMode.CLAMP :tmy));
                }

                state.mRebuildShader = false;
                copyBounds(mDstRect);
            }

            Shader shader = state.mPaint.getShader();

            if (shader == null )
            {
                if (mApplyGravity)
                {
                    Gravity.apply(state.mGravity, mBitmapWidth, mBitmapHeight,getBounds(),mDstRect, View.LAYOUT_DIRECTION_LTR);
                    mApplyGravity = false;
                }

                if (needMirroring())
                {
                    updateMirrorMatrix(mDstRect.right - mDstRect.left);
                    shader.setLocalMatrix(mMirrorMatrix);
                }
                else
                {
                    if (mMirrorMatrix != null)
                    {
                        mMirrorMatrix = null;
                        shader.setLocalMatrix(new Matrix());
                    }
                }

                canvas.drawBitmap(bitmap, null, mDstRect, state.mPaint);
            }
            else
            {
                if (mApplyGravity)
                {
                    copyBounds(mDstRect);
                    mApplyGravity = false;
                }

                if (needMirroring())
                {
                    updateMirrorMatrix(mDstRect.right - mDstRect.left);
                    shader.setLocalMatrix(mMirrorMatrix);
                }
                else
                {
                    if (mMirrorMatrix != null)
                    {
                        mMirrorMatrix = null;
                        shader.setLocalMatrix(new Matrix());
                    }
                }

                canvas.drawRect(mDstRect,state.mPaint);
            }

        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getOpacity() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void inflate(Resources r, XmlPullParser parser,AttributeSet attrs)
            throws XmlPullParserException, IOException
    {
        int attrsCount = attrs.getAttributeCount();

        Paint localPaint = new Paint();
        for (int i = 0; i < attrsCount; i++)
        {
            String attrsName = attrs.getAttributeName(i);
            if (attrsName.equalsIgnoreCase("src"))
            {

                String  resName = attrs.getAttributeValue(i);

                TypedValue paramTypedValue = new TypedValue();
                String[] attrvalues =   resName.split("/");
                String attrType = attrvalues[0];
                attrType = attrType.replaceAll("@","");
                String attrName = attrvalues[1];
                int resId = r.getIdentifier(attrName,attrType,"com.BeeFramework.example");
                int resId2 = ResourcesFactory.getResId(attrName,R.drawable.class);

                r.getValue(resId,paramTypedValue,true);
                String fileName = paramTypedValue.string.toString();
                String absolutePath = ThemeManager.getInstance().getThemeFilePath() +"/"+ fileName;

                final Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                if (bitmap == null)
                {
                        throw new XmlPullParserException(parser.getPositionDescription() +
                                ": <bitmap> requires a valid src attribute");
                }
                else
                {
                    mBitmap = bitmap;
                }
            }
            else if (attrsName.equalsIgnoreCase("antialias"))
            {
                localPaint.setAntiAlias(attrs.getAttributeBooleanValue(i,localPaint.isAntiAlias()));
            }
            else if (attrsName.equalsIgnoreCase("filter"))
            {
                localPaint.setFilterBitmap(attrs.getAttributeBooleanValue(i,localPaint.isFilterBitmap()));
            }
            else if (attrsName.equalsIgnoreCase("dither"))
            {
                localPaint.setDither(attrs.getAttributeBooleanValue(i,localPaint.isDither()));
            }
            else if (attrsName.equalsIgnoreCase("gravity"))
            {
               int gravity = attrs.getAttributeIntValue(i, Gravity.FILL);
                setGravity(gravity);
            }
            else if (attrsName.equalsIgnoreCase("tileMode"))
            {
                int tileMode = attrs.getAttributeIntValue(i,-1);
                if (tileMode != -1)
                {
                    switch (tileMode)
                    {
                        case 0:
                            setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                            break;
                        case 1:
                            setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                            break;
                        case 2:
                            setTileModeXY(Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    }
                }
            }
            Log.d(attrsName);
        }
    }

    final static public class BeeBitmapState extends ConstantState
    {
        Bitmap mBitmap;
        int mChangingConfigurations;
        int mGravity = new Gravity().FILL;
        Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);
        Shader.TileMode mTileModeX = null;
        Shader.TileMode mTileModeY = null;

        int mTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        boolean mRebuildShader;
        boolean mAutoMirrored;

        BeeBitmapState(Bitmap bitmap)
        {
            mBitmap = bitmap;
        }


        BeeBitmapState(BeeBitmapState bitmapState)
        {
            this(bitmapState.mBitmap);
            mChangingConfigurations = bitmapState.mChangingConfigurations;
            mGravity = bitmapState.mGravity;
            mTileModeX = bitmapState.mTileModeX;
            mTileModeY = bitmapState.mTileModeY;
            mTargetDensity = bitmapState.mTargetDensity;
            mPaint = new Paint(bitmapState.mPaint);
            mRebuildShader = bitmapState.mRebuildShader;
            mAutoMirrored = bitmapState.mAutoMirrored;
        }

        public Bitmap getBitmap()
        {
            return mBitmap;
        }
        @Override
        public Drawable newDrawable() {
            return new BeeBitmapDrawable(this,null);
        }

        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }
}
