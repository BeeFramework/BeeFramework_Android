package com.example.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
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
public class PLAYER extends Model {
	
	public int id;
	public String name;
	public String location;
	public int followers_count;
	public int draftees_conut;
	public int likes_count;
	public int likes_receiced_count;
	public int rebounds_count;
	public int rebounds_received_count;
	public String url;
	public String avatar_url;
	public String username;
	public String twitter_screen_name;
	public String website_url;
	public int drafted_by_player_id;
	public int shots_count;
	public int following_count;
	public String created_at;
	
	public void  fromJson(JSONObject jsonObject)  throws JSONException {
         if(null == jsonObject){
        	 return;
         }
         this.id = jsonObject.optInt("id");
         this.name = jsonObject.optString("name");
         this.location = jsonObject.optString("location");
         this.followers_count = jsonObject.optInt("followers_count");
         this.draftees_conut = jsonObject.optInt("draftees_conut");
         this.likes_count = jsonObject.optInt("likes_count");
         this.likes_receiced_count = jsonObject.optInt("likes_receiced_count");
         this.rebounds_count = jsonObject.optInt("rebounds_count");
         this.rebounds_received_count = jsonObject.optInt("rebounds_received_count");
         this.url = jsonObject.optString("url");
         this.avatar_url = jsonObject.optString("avatar_url");
         this.username = jsonObject.optString("username");
         this.twitter_screen_name = jsonObject.optString("twitter_screen_name");
         this.website_url = jsonObject.optString("website_url");
         this.drafted_by_player_id = jsonObject.optInt("drafted_by_player_id");
         this.shots_count = jsonObject.optInt("shots_count");
         this.following_count = jsonObject.optInt("following_count");
         this.created_at = jsonObject.optString("created_at");
         return ;
    }
	
	public JSONObject  toJson() throws JSONException {
         JSONObject localItemObject = new JSONObject();
         localItemObject.put("id", id);
         localItemObject.put("name", name);
         localItemObject.put("location", location);
         localItemObject.put("followers_count", followers_count);
         localItemObject.put("draftees_conut", draftees_conut);
         localItemObject.put("likes_count", likes_count);
         localItemObject.put("likes_receiced_count", likes_receiced_count);
         localItemObject.put("rebounds_count", rebounds_count);
         localItemObject.put("rebounds_received_count", rebounds_received_count);
         localItemObject.put("url", url);
         localItemObject.put("avatar_url", avatar_url);
         localItemObject.put("username", username);
         localItemObject.put("twitter_screen_name", twitter_screen_name);
         localItemObject.put("website_url", website_url);
         localItemObject.put("drafted_by_player_id", drafted_by_player_id);
         localItemObject.put("shots_count", shots_count);
         localItemObject.put("following_count", following_count);
         localItemObject.put("created_at", created_at);
         return localItemObject;
    }
	
}
