import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

public class JsonSparqlResult {
    public List<List<ResultItem>> data = new ArrayList<List<ResultItem>>();
    public List<String> head = new ArrayList<String>();

    public JsonSparqlResult(String json) throws Exception {
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
                        if (rowItem.get("value") != null)
                            sValue = ((String) rowItem.get("value")).trim();
                        String sType = "";
                        if (rowItem.get("type") != null)
                            sType = ((String) rowItem.get("type")).trim();
                        String sLang = "";
                        if (rowItem.get("xml:lang") != null)
                            sLang = ((String) rowItem.get("xml:lang")).trim();
                        String sDatatype = "";
                        if (rowItem.get("datatype") != null)
                            sDatatype = ((String) rowItem.get("datatype")).trim();
                        item.add(new ResultItem(sValue, sType, sLang, sDatatype));
                    }
                    data.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public String getFirstResult() {
        if (data.size() > 0 && data.get(0).size() > 0)
            return data.get(0).get(0).value;
        return "";
    }
}
