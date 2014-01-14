package com.BeeFramework.theme;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.*;
import android.util.AttributeSet;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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
public class BeeDrawable extends Drawable
{

    public void draw(Canvas canvas)
    {

    }

    @Override
    public void setAlpha(int alpha)
    {

    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {

    }

    @Override
    public int getOpacity()
    {
        return 0;
    }

    public static BeeDrawable createDrawableFromXML(Resources r, XmlPullParser parser)
            throws XmlPullParserException,IOException
    {
        AttributeSet attributeSet = Xml.asAttributeSet(parser);
        int type;
        while ((type=parser.next()) != XmlPullParser.START_TAG &&
                type != XmlPullParser.END_DOCUMENT) {
            // Empty loop
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }
       return BeeDrawable.createFromXmlInner(r,parser,attributeSet);

    }

    public static BeeDrawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs)
            throws XmlPullParserException, IOException {
        Drawable drawable = null;

        final String name = parser.getName();

        if (name.equals("selector"))
        {
            drawable = new StateListDrawable();
        }
        else if (name.equals("level-list"))
        {
            drawable = new LevelListDrawable();
        }
        else if (name.equals("layer-list"))
        {
           // drawable = new LayerDrawable();
        }
        else if (name.equals("transition"))
        {
            //drawable = new TransitionDrawable();
        }
        else if (name.equals("color"))
        {
            drawable = new ColorDrawable();
        }
        else if (name.equals("shape"))
        {
            drawable = new GradientDrawable();
        }
        else if (name.equals("scale"))
        {
           // drawable = new ScaleDrawable();
        }
        else if (name.equals("clip"))
        {
            //drawable = new ClipDrawable();
        }
        else if (name.equals("rotate"))
        {
            drawable = new RotateDrawable();
        }
        else if (name.equals("animated-rotate"))
        {
            //drawable = new AnimatedRotateDrawable();
        }
        else if (name.equals("animation-list"))
        {
            drawable = new AnimationDrawable();
        }
        else if (name.equals("inset"))
        {
            //drawable = new InsetDrawable();
        }
        else if (name.equals("bitmap"))
        {
            //noinspection deprecation
            drawable = new BeeBitmapDrawable(r);
            if (r != null) {
                ((BeeBitmapDrawable) drawable).setTargetDensity(r.getDisplayMetrics());
            }
        }
        else if (name.equals("nine-patch"))
        {
//            drawable = new NinePatchDrawable();
//            if (r != null)
//            {
//                ((NinePatchDrawable) drawable).setTargetDensity(r.getDisplayMetrics());
//            }
        }
        else
        {
            throw new XmlPullParserException(parser.getPositionDescription() +
                    ": invalid drawable tag " + name);
        }

        drawable.inflate(r, parser, attrs);
        return (BeeDrawable)drawable;
    }
}
