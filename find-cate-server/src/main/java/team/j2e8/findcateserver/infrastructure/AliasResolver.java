package team.j2e8.findcateserver.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

abstract class AliasResolver {
    String resolveAlias(Object object, String propertyName) throws NoSuchFieldException {
        Class<?> classType = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Optional<Field> field = Arrays.stream(classType.getDeclaredFields()).filter(f -> {
            JsonProperty jsonProperty = f.getAnnotation(JsonProperty.class);
            return jsonProperty != null && jsonProperty.value().equals(propertyName);
        }).findFirst();
        String alias = field.isPresent() ? field.get().getName() : propertyName;
        JsonProperty jsonProperty = classType.getDeclaredField(alias).getAnnotation(JsonProperty.class);
        return (jsonProperty != null && jsonProperty.access() == JsonProperty.Access.WRITE_ONLY) ? null : alias;
    }
}
