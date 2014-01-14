package com.BeeFramework.theme;

import android.content.res.Resources;
import android.util.TypedValue;
import com.BeeFramework.BeeFrameworkApp;
import com.external.activeandroid.util.Log;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
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
public class ThemeManager
{
    Resources mResources;
    String themeFilePath;
    private static ThemeManager instance;

    public static ThemeManager getInstance()
    {
        if (null == instance)
        {
            instance = new ThemeManager(null);
        }
        return instance;
    }

    public void setThemeRootPath(String fileDir)
    {
        themeFilePath = fileDir;
    }

    public String getThemeFilePath()
    {
        return themeFilePath;
    }

    public ThemeManager(Resources resources)
    {
        mResources = resources;
    }

    public XmlPullParser getResourceParser(int resourceId, TypedValue paramTypedValue) throws IOException
    {
       this.mResources.getValue(resourceId,paramTypedValue,true);
        if (paramTypedValue.type == TypedValue.TYPE_STRING)
        {
            if (null != themeFilePath)
            {
                String xmlPath = themeFilePath + paramTypedValue.string.toString();
                XmlPullParser temp = this.mResources.getAssets().openXmlResourceParser(xmlPath);
            }
            else
            {
                return this.mResources.getAssets().openXmlResourceParser(paramTypedValue.assetCookie,paramTypedValue.string.toString());
            }
        }

        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(resourceId) + " type #0x" + Integer.toHexString(paramTypedValue.type) + " is not valid");
    }
}
