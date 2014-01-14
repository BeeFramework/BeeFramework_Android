package com.example.protocol;

import com.external.activeandroid.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class COMMENT extends Model {
    public int id;
    public int likes_count;
    public String body;
    public String created_at;
    public PLAYER player;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return;
        }
        this.id = jsonObject.optInt("id");
        this.likes_count = jsonObject.optInt("likes_count");
        this.body=jsonObject.optString("body");
        this.created_at = jsonObject.optString("created_at");
        PLAYER player=new PLAYER();
        player.fromJson(jsonObject.optJSONObject("player"));
        this.player=player;
        return;
    }
    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("id", id);
        localItemObject.put("likes_count", likes_count);
        localItemObject.put("body",body);
        localItemObject.put("created_at", created_at);
        if(null != player)
        {
            localItemObject.put("player", this.player.toJson());
        }
        return localItemObject;
    }
}
