
package com.example.protocol;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "USER")
public class USER  extends Model
{
     @Column(name = "location")
     public String   location;

     @Column(name = "remark")
     public String   remark;

     @Column(name = "verified_reason")
     public String   verified_reason;

     @Column(name = "statuses_count")
     public int statuses_count;

     @Column(name = "city")
     public String   city;

     @Column(name = "USER_id")
     public int id;

     @Column(name = "following")
     public boolean following;

     @Column(name = "favourites_count")
     public int favourites_count;

     @Column(name = "description")
     public String   description;

     @Column(name = "verified")
     public boolean verified;

     @Column(name = "name")
     public String   name;

     @Column(name = "domain")
     public String   domain;

     @Column(name = "province")
     public String   province;

     @Column(name = "gender")
     public String   gender;

     @Column(name = "created_at")
     public String   created_at;

     @Column(name = "followers_count")
     public int followers_count;

     @Column(name = "online_status")
     public int online_status;

     @Column(name = "bi_followers_count")
     public int bi_followers_count;

     @Column(name = "geo_enabled")
     public boolean geo_enabled;

     @Column(name = "allow_all_comment")
     public boolean allow_all_comment;

     @Column(name = "allow_all_act_msg")
     public boolean allow_all_act_msg;

     @Column(name = "url")
     public String   url;

     @Column(name = "avatar_large")
     public String   avatar_large;

     @Column(name = "friends_count")
     public int friends_count;

     @Column(name = "screen_name")
     public String   screen_name;

     @Column(name = "profile_image_url")
     public String   profile_image_url;

     @Column(name = "follow_me")
     public boolean follow_me;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.location = jsonObject.optString("location");

          this.remark = jsonObject.optString("remark");

          this.verified_reason = jsonObject.optString("verified_reason");

          this.statuses_count = jsonObject.optInt("statuses_count");

          this.city = jsonObject.optString("city");

          this.id = jsonObject.optInt("id");

          this.following = jsonObject.optBoolean("following");

          this.favourites_count = jsonObject.optInt("favourites_count");

          this.description = jsonObject.optString("description");

          this.verified = jsonObject.optBoolean("verified");

          this.name = jsonObject.optString("name");

          this.domain = jsonObject.optString("domain");

          this.province = jsonObject.optString("province");

          this.gender = jsonObject.optString("gender");

          this.created_at = jsonObject.optString("created_at");

          this.followers_count = jsonObject.optInt("followers_count");

          this.online_status = jsonObject.optInt("online_status");

          this.bi_followers_count = jsonObject.optInt("bi_followers_count");

          this.geo_enabled = jsonObject.optBoolean("geo_enabled");

          this.allow_all_comment = jsonObject.optBoolean("allow_all_comment");

          this.allow_all_act_msg = jsonObject.optBoolean("allow_all_act_msg");

          this.url = jsonObject.optString("url");

          this.avatar_large = jsonObject.optString("avatar_large");

          this.friends_count = jsonObject.optInt("friends_count");

          this.screen_name = jsonObject.optString("screen_name");

          this.profile_image_url = jsonObject.optString("profile_image_url");

          this.follow_me = jsonObject.optBoolean("follow_me");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("location", location);
          localItemObject.put("remark", remark);
          localItemObject.put("verified_reason", verified_reason);
          localItemObject.put("statuses_count", statuses_count);
          localItemObject.put("city", city);
          localItemObject.put("id", id);
          localItemObject.put("following", following);
          localItemObject.put("favourites_count", favourites_count);
          localItemObject.put("description", description);
          localItemObject.put("verified", verified);
          localItemObject.put("name", name);
          localItemObject.put("domain", domain);
          localItemObject.put("province", province);
          localItemObject.put("gender", gender);
          localItemObject.put("created_at", created_at);
          localItemObject.put("followers_count", followers_count);
          localItemObject.put("online_status", online_status);
          localItemObject.put("bi_followers_count", bi_followers_count);
          localItemObject.put("geo_enabled", geo_enabled);
          localItemObject.put("allow_all_comment", allow_all_comment);
          localItemObject.put("allow_all_act_msg", allow_all_act_msg);
          localItemObject.put("url", url);
          localItemObject.put("avatar_large", avatar_large);
          localItemObject.put("friends_count", friends_count);
          localItemObject.put("screen_name", screen_name);
          localItemObject.put("profile_image_url", profile_image_url);
          localItemObject.put("follow_me", follow_me);
          return localItemObject;
     }

}
