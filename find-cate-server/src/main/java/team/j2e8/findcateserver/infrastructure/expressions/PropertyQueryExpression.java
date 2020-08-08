package team.j2e8.findcateserver.infrastructure.expressions;


public class PropertyQueryExpression extends QueryExpression {

    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String propertyName) {
        this.property = propertyName;
    }
}
