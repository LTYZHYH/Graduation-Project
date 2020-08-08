package team.j2e8.findcateserver.infrastructure;

import team.j2e8.findcateserver.annotation.Constraint;
import team.j2e8.findcateserver.annotation.FieldProcessor;
import team.j2e8.findcateserver.exceptions.InvalidObjectSelectionException;
import team.j2e8.findcateserver.infrastructure.expressions.ObjectQueryExpression;
import team.j2e8.findcateserver.infrastructure.expressions.PropertyQueryExpression;
import team.j2e8.findcateserver.infrastructure.expressions.QueryExpression;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;


public class ObjectSelector extends AliasResolver {


    private QueryExpressionParser parser;

    public ObjectSelector() {
        this.parser = new QueryExpressionParser();
    }

    public Page<Map<String, Object>> mapPagedObjects(Page<?> objects, String query) {
        ObjectQueryExpression expression = this.parser.parseObjectQuery(query);
        List<Map<String, Object>> mappedObjects = new ArrayList<>();
        for (Object object : objects) {
            mappedObjects.add(this.mapObject(object, expression));
        }
        return new PageImpl<>(mappedObjects, PageRequest.of(objects.getNumber(),
                objects.getSize(), objects.getSort()), objects.getTotalElements());
    }


    public List<Map<String, Object>> mapObjects(Iterable<?> objects, String query) {
        ObjectQueryExpression expression = this.parser.parseObjectQuery(query);
        List<Map<String, Object>> mappedObjects = new ArrayList<>();
        for (Object object : objects) {
            mappedObjects.add(this.mapObject(object, expression));
        }
        return mappedObjects;
    }

    public Map<String, Object> mapObject(Object object, String query) {
        ObjectQueryExpression expression = this.parser.parseObjectQuery(query);
        return this.mapObject(object, expression);
    }

    public Map<String, Object> mapObject(Object object, ObjectQueryExpression expression) {
        try {
            Map<String, Object> propertyMap = new HashMap<>();
            PropertyDescriptor[] objectProperties = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
            for (QueryExpression queryExpression : expression.getProperties()) {
                if (queryExpression instanceof PropertyQueryExpression) {
                    PropertyQueryExpression propertyExpression = (PropertyQueryExpression) queryExpression;
                    String name = this.resolveAlias(object, propertyExpression.getProperty());
                    if (name == null) {
                        continue;
                    }
                    Optional<PropertyDescriptor> fieldProperty = Arrays.stream(objectProperties).filter(p -> p.getName()
                            .equals(name)).findFirst();
                    if (fieldProperty.isPresent()) {
                        Object fieldValue = fieldProperty.get().getReadMethod().invoke(object);
                        propertyMap.put(propertyExpression.getProperty(), processProperty(object, fieldValue, name));
                    }
                } else if (queryExpression instanceof ObjectQueryExpression) {
                    ObjectQueryExpression objectQueryExpression = (ObjectQueryExpression) queryExpression;
                    String name = this.resolveAlias(object, objectQueryExpression.getObject());
                    if (name == null) {
                        continue;
                    }
                    Optional<PropertyDescriptor> fieldProperty = Arrays.stream(objectProperties).filter(p -> p.getName()
                            .equals(name)).findFirst();
                    if (fieldProperty.isPresent()) {
                        Object propertyValue = fieldProperty.get().getReadMethod().invoke(object);
                        if (propertyValue == null) {
                            propertyMap.put(objectQueryExpression.getObject(), null);
                        } else if (propertyValue instanceof Iterable) {
                            // 如果是集合对象，对每一个元素进行迭代处理
                            List<Object> childObjects = new ArrayList<>();
                            for (Object childObject : (Iterable) propertyValue) {
                                childObjects.add(this.mapObject(childObject, objectQueryExpression));
                            }
                            propertyMap.put(objectQueryExpression.getObject(), childObjects);
                        } else {
                            propertyMap.put(objectQueryExpression.getObject(),
                                    this.mapObject(fieldProperty.get().getReadMethod().invoke(object), objectQueryExpression));
                        }
                    }
                } else {
                    // we should never get here
                    throw new UnsupportedOperationException(queryExpression.toString());
                }
            }
            return propertyMap;
        } catch (Exception ex) {
            throw new InvalidObjectSelectionException(expression.getRawQuery());
        }
    }

    private Object processProperty(Object o, Object value, String name) throws NoSuchFieldException {
        Class<?> classType = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Constraint constraint = classType.getDeclaredField(name).getAnnotation(Constraint.class);
        if (constraint != null) {
            Class<? extends FieldProcessor>[] fieldProcessors = constraint.processBy();
            for (Class<? extends FieldProcessor> fieldProcessor : fieldProcessors) {
                try {
                    value = fieldProcessor.newInstance().process(o, value, name);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new UnsupportedOperationException("不支持的objectSelection处理");
                }
            }
        }
        return value;
    }
}
