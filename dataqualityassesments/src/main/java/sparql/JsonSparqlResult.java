package sparql;

public class JsonSparqlResult {
//	public List<List<ResultItem>> data = new ArrayList<List<ResultItem>>();
//	public List<String> head = new ArrayList<String>();
//
//	public JsonSparqlResult(String json) throws Exception {
//		try {
//			JSONValue value = JSONParser.parseLenient(json);
//			JSONObject obj = value.isObject();
//
//			JSONObject headObj = obj.get("head").isObject();
//			JSONArray headVars = headObj.get("vars").isArray();
//			if (headVars != null) {
//				for (int i = 0; i < headVars.size(); i++) {
//					String varName = headVars.get(i).isString().stringValue();
//					head.add(varName);
//				}
//			}
//
//			JSONObject resultsObj = obj.get("results").isObject();
//			JSONArray bindings = resultsObj.get("bindings").isArray();
//			if (bindings != null) {
//				for (int i = 0; i < bindings.size(); i++) {
//
//					JSONObject current = bindings.get(i).isObject();
//					List<ResultItem> item = new ArrayList<ResultItem>();
//					for (int j = 0; j < head.size(); j++) {
//						JSONObject rowItem = current.get(head.get(j)).isObject();
//						String sValue = "";
//						if (rowItem.get("value") != null)
//							sValue = rowItem.get("value").isString().stringValue().trim();
//						String sType = "";
//						if (rowItem.get("type") != null)
//							sType = rowItem.get("type").isString().stringValue().trim();
//						String sLang = "";
//						if (rowItem.get("xml:lang") != null)
//							sLang = rowItem.get("xml:lang").isString().stringValue().trim();
//						String sDatatype = "";
//						if (rowItem.get("datatype") != null)
//							sDatatype = rowItem.get("datatype").isString().stringValue().trim();
//						item.add(new ResultItem(sValue, sType, sLang, sDatatype));
//					}
//					data.add(item);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception();
//		}
//
//	}
//
//	public String getFirstResult() {
//        if (data.size() > 0 && data.get(0).size() > 0)
//            return data.get(0).get(0).value;
//        return "";
//
//    }
}
