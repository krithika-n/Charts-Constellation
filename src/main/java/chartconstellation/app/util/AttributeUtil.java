package chartconstellation.app.util;

import chartconstellation.app.entities.FeatureDistance;
import chartconstellation.app.entities.IdValue;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AttributeUtil {

    public double jaccardSimilarity(Set<String> s1, Set<String> s2) {

        final int sa = s1.size();
        final int sb = s2.size();
        s1.retainAll(s2);
        final int intersection = s1.size();
        return 1d / (sa + sb - intersection) * intersection;
    }

    public Set<String> getAllAttributes(JSONObject jsonObj) {

        Set<String> attrs = new HashSet<>();

        try {
            String attr = jsonObj.getJSONObject("encoding").getJSONObject("x").get("field").toString();
            attrs.add(attr);
        } catch(Exception e) {

        }

        try {

            JSONArray arr = jsonObj.getJSONArray("layer");
            for(int i=0 ;i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String attr = obj.getJSONObject("encoding").getJSONObject("x").get("field").toString();
                attrs.add(attr);
            }

        } catch(Exception e) {

        }
        return attrs;
    }

    public List<FeatureDistance> computerAttributeDistance(DBCollection collection) {

        DBCursor cursor = collection.find();
        List<DBObject> docs = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            docs.add(obj);
        }

        List<FeatureDistance> distances = new ArrayList<>();

        for (DBObject obj1 : docs) {
            FeatureDistance indDistance = new FeatureDistance();
            int id1 = (int) obj1.get("id");
            indDistance.setId(String.valueOf(id1));
            List<IdValue> values = new ArrayList<>();
            for (DBObject obj2 : docs) {

                int id2 = (int) obj2.get("id");
                if (id1 != id2) {
                    try {
                        JSONObject jsonObj1 = new JSONObject(obj1.toString());
                        JSONObject jsonObj2 = new JSONObject(obj2.toString());

                        Double val = jaccardSimilarity(
                                getAllAttributes(jsonObj1),
                                getAllAttributes(jsonObj2)
                        );

                        values.add(new IdValue(String.valueOf(id2), val));

                    } catch (Exception e) {

                    }
                }
            }
            indDistance.setIdValues(values);
            distances.add(indDistance);
        }

        return distances;
    }

}
