package team.j2e8.findcateserver.infrastructure;

import team.j2e8.findcateserver.exceptions.ArgumentErrorException;
import team.j2e8.findcateserver.exceptions.InvalidObjectSelectionException;
import team.j2e8.findcateserver.infrastructure.expressions.ObjectQueryExpression;
import team.j2e8.findcateserver.infrastructure.expressions.QueryExpression;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;


public class ObjectPropertiesTrimmer extends AliasResolver {

    private QueryExpressionParser parser;

    public ObjectPropertiesTrimmer() {
        this.parser = new QueryExpressionParser();
    }

    public void trimProperties(Object object, String objectQuery) {
        ObjectQueryExpression objectQueryExpression = parser.parseObjectQuery(objectQuery);
        trimProperties(object, objectQueryExpression);
    }

    private void trimProperties(Object object, ObjectQueryExpression objectQueryExpression) {
        try {
            PropertyDescriptor[] objectProperties = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
            for (QueryExpression queryExpression : objectQueryExpression.getProperties()) {
                String name = this.resolveAlias(object, ((ObjectQueryExpression) queryExpression).getObject());
                if (name == null) {
                    continue;
                }
                Optional<PropertyDescriptor> fieldProperty = Arrays.stream(objectProperties).filter(p -> p.getName()
                        .equals(name)).findFirst();
                if (!fieldProperty.isPresent()) {
                    throw new ArgumentErrorException("trim表达式错误");
                }
                Object propertyValue = fieldProperty.get().getReadMethod().invoke(object);
                if (propertyValue instanceof Iterable) {
                    // 如果是集合对象，对每一个元素进行迭代处理
                    for (Object childObject : (Iterable) propertyValue) {
                        this.trimProperties(childObject, (ObjectQueryExpression) queryExpression);
                    }
                } else {
                    this.trimProperties(propertyValue, (ObjectQueryExpression) queryExpression);
                }
            }

            for (PropertyDescriptor objectProperty : objectProperties) {
                if (objectProperty.getReadMethod() == null || objectProperty.getReadMethod().getName().equals("getClass")){
                    continue;
                }
                Object o = objectProperty.getReadMethod().invoke(object);
                if (o != null && o instanceof String) {
                    String value = (String) objectProperty.getReadMethod().invoke(object);
                    if (value != null) {
                        objectProperty.getWriteMethod().invoke(object, value.trim());
                    }
                }
            }
        } catch (IllegalAccessException | IntrospectionException | NoSuchFieldException | InvocationTargetException e) {
            throw new InvalidObjectSelectionException(objectQueryExpression.getRawQuery());
        }
    }
}
