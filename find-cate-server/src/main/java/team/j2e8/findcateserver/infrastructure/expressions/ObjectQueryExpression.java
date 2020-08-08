package team.j2e8.findcateserver.infrastructure.expressions;

import java.util.ArrayList;
import java.util.List;


public class ObjectQueryExpression extends QueryExpression {


    private List<QueryExpression> properties = new ArrayList<>();
    private String object;

    public List<QueryExpression> getProperties() {
        return properties;
    }

    public void setProperties(List<QueryExpression> properties) {
        this.properties = properties;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    private String rawQuery;

}
