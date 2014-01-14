package com.example.protocol;

import android.media.AsyncPlayer;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
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

@Table(name = "SHOT")
public class SHOT extends Model
{
    @Column(name = "shot_id")
    public int id;

    @Column(name = "title")
    public String title;

    @Column(name = "url")
    public String url;

    @Column(name = "short_url")
    public String short_url;

    @Column(name = "image_url")
    public String image_url;

    @Column(name = "image_teaser_url")
    public String image_teaser_url;

    @Column(name = "width")
    public int width;

    @Column(name = "height")
    public int height;

    @Column(name = "views_count")
    public int views_count;

    @Column(name = "likes_count")
    public int likes_count;

    @Column(name = "comments_count")
    public int comments_count;

    @Column(name = "rebounds_count")
    public int rebounds_count;

    @Column(name = "rebounds_source_id")
    public int rebounds_source_id;

    @Column(name = "created_at")
    public String created_at;

    public PLAYER player;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }

        JSONArray subItemArray;

        this.id = jsonObject.optInt("id");

        this.title = jsonObject.optString("title");

        this.url = jsonObject.optString("url");

        this.short_url = jsonObject.optString("short_url");

        this.image_url = jsonObject.optString("image_url");

        this.image_teaser_url = jsonObject.optString("image_teaser_url");

        this.width = jsonObject.optInt("width");

        this.height = jsonObject.optInt("height");

        this.views_count = jsonObject.optInt("views_count");

        this.likes_count = jsonObject.optInt("likes_count");

        this.comments_count = jsonObject.optInt("comments_count");

        this.rebounds_count = jsonObject.optInt("rebounds_count");

        this.rebounds_source_id = jsonObject.optInt("rebounds_source_id");

        this.created_at = jsonObject.optString("created_at");

        this.player = new PLAYER();
        this.player.fromJson(jsonObject.optJSONObject("player"));
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("url", url);
        localItemObject.put("short_url", short_url);
        localItemObject.put("image_url", image_url);
        localItemObject.put("image_teaser_url", image_teaser_url);
        localItemObject.put("width", width);
        localItemObject.put("height", height);
        localItemObject.put("views_count", views_count);
        localItemObject.put("likes_count", likes_count);
        localItemObject.put("comments_count", comments_count);
        localItemObject.put("rebounds_count", rebounds_count);
        localItemObject.put("rebounds_source_id", rebounds_source_id);
        localItemObject.put("created_at", created_at);
        localItemObject.put("player", this.player.toJson());

        return localItemObject;
    }

}
