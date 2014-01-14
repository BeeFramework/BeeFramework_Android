package com.BeeFramework.theme;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.jar.Attributes;
import java.io.FileInputStream;

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
public class ResourcesFactory
{
    static BeeDrawable createDrawableFromXml(Resources resources, XmlPullParser xmlPullParser)
        throws XmlPullParserException,IOException
    {
        AttributeSet localAttributeSet1 = Xml.asAttributeSet(xmlPullParser);
        return null;
    }


    public static Drawable getDrawable(Resources resources, int resId)
    {
        TypedValue paramTypedValue = new TypedValue();
        BeeDrawable dr = null;
        resources.getValue(resId,paramTypedValue,true);
        if (paramTypedValue.string.toString().endsWith("xml"))
        {
           //TODO
            String fileName = paramTypedValue.string.toString();
            String absolutePath = ThemeManager.getInstance().getThemeFilePath() +"/"+ fileName;
            try
            {
                FileInputStream inputStream = new FileInputStream(absolutePath);
                XmlPullParser xmlParser = Xml.newPullParser();
                xmlParser.setInput(inputStream,"UTF-8");
                dr = (BeeDrawable) BeeDrawable.createDrawableFromXML(resources, xmlParser);
            }
            catch (XmlPullParserException e1)
            {
                e1.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return dr;
        }
        else
        {
            String absolutePath = ThemeManager.getInstance().getThemeFilePath() + paramTypedValue.string.toString();

            try {
                InputStream is = resources.getAssets().open(absolutePath, AssetManager.ACCESS_STREAMING);
                dr = (BeeDrawable) Drawable.createFromStream(is, paramTypedValue.string.toString());
                is.close();
                return dr;
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return dr;
        }
    }

    /**
     * @author Lonkly
     * @param variableName - name of drawable, e.g R.drawable.<b>image</b>
     * @param с - class of resource, e.g R.drawable, of R.raw
     * @return integer id of resource
     */
    public static int getResId(String variableName, Class<?> с) {

        Field field = null;
        int resId = 0;
        try {
            field = с.getField(variableName);
            try {
                resId = field.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;

    }

}







