package com.itba.sparql;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonSparqlResult {
    public List<List<ResultItem>> data = new ArrayList<List<ResultItem>>();
    public List<String> head = new ArrayList<String>();

    public JsonSparqlResult(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            JSONObject headObj = (JSONObject) obj.get("head");
            JSONArray headVars = (JSONArray) headObj.get("vars");
            if (headVars != null) {
                for (int i = 0; i < headVars.length(); i++) {
                    String varName = headVars.getString(i);
                    head.add(varName);
                }
            }

            JSONObject resultsObj = (JSONObject) obj.get("results");
            JSONArray bindings = (JSONArray) resultsObj.get("bindings");
            if (bindings != null) {
                for (int i = 0; i < bindings.length(); i++) {

                    JSONObject current = (JSONObject) bindings.get(i);
                    List<ResultItem> item = new ArrayList<ResultItem>();
                    for (int j = 0; j < head.size(); j++) {
                        JSONObject rowItem = (JSONObject) current.get(head.get(j));
                        String sValue = "";
                        if (rowItem.has("value"))
                            sValue = ((String) rowItem.get("value")).trim();
                        String sType = "";
                        if (rowItem.has("type"))
                            sType = ((String) rowItem.get("type")).trim();
                        String sLang = "";
                        if (rowItem.has("xml:lang"))
                            sLang = ((String) rowItem.get("xml:lang")).trim();
                        String sDatatype = "";
                        if (rowItem.has("datatype"))
                            sDatatype = ((String) rowItem.get("datatype")).trim();
                        item.add(new ResultItem(sValue, sType, sLang, sDatatype));
                    }
                    data.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFirstResult() {
        if (data.size() > 0 && data.get(0).size() > 0)
            return data.get(0).get(0).value;
        return "";
    }
}
