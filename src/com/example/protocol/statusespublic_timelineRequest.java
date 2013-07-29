
package com.example.protocol;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "statusespublic_timelineRequest")
public class statusespublic_timelineRequest  extends Model
{

     @Column(name = "count")
     public int count;

     @Column(name = "source")
     public String   source;

     @Column(name = "access_token")
     public String   access_token;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.count = jsonObject.optInt("count");

          this.source = jsonObject.optString("source");

          this.access_token = jsonObject.optString("access_token");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("count", count);
          localItemObject.put("source", source);
          localItemObject.put("access_token", access_token);
          return localItemObject;
     }

}
