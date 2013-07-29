
package com.example.protocol;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Table(name = "statusespublic_timelineResponse")
public class statusespublic_timelineResponse  extends Model
{

     @Column(name = "total_number")
     public int total_number;

     public ArrayList<STATUSES>   statuses = new ArrayList<STATUSES>();

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.total_number = jsonObject.optInt("total_number");

          subItemArray = jsonObject.optJSONArray("statuses");
          if(null != subItemArray)
           {
              for(int i = 0;i < subItemArray.length();i++)
               {
                  JSONObject subItemObject = subItemArray.getJSONObject(i);
                  STATUSES subItem = new STATUSES();
                  subItem.fromJson(subItemObject);
                  this.statuses.add(subItem);
               }
           }

          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("total_number", total_number);

          for(int i =0; i< statuses.size(); i++)
          {
              STATUSES itemData =statuses.get(i);
              JSONObject itemJSONObject = itemData.toJson();
              itemJSONArray.put(itemJSONObject);
          }
          localItemObject.put("statuses", itemJSONArray);
          return localItemObject;
     }

}
