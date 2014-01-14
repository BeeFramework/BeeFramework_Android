package com.BeeFramework.theme;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

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
public class BeeXMLParser implements XmlPullParser {
    @Override
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getFeature(String name) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setProperty(String name, Object value) throws XmlPullParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getProperty(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setInput(Reader in) throws XmlPullParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInputEncoding() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNamespace(String prefix) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDepth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPositionDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLineNumber() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getColumnNumber() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        return new char[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNamespace() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPrefix() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAttributeCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeNamespace(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeName(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributePrefix(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeType(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAttributeDefault(int index) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeValue(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int next() throws XmlPullParserException, IOException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
