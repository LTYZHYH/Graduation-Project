package team.j2e8.findcateserver.annotation;


public interface FieldProcessor {
    Object process(Object o, Object value, String fieldName);
}
