package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QType is a Querydsl query type for Type
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QType extends EntityPathBase<Type> {

    private static final long serialVersionUID = -1466697859L;

    public static final QType type = new QType("type1");

    public final SetPath<Food, QFood> foods = this.<Food, QFood>createSet("foods", Food.class, QFood.class, PathInits.DIRECT2);

    public final SetPath<Shop, QShop> shops = this.<Shop, QShop>createSet("shops", Shop.class, QShop.class, PathInits.DIRECT2);

    public final NumberPath<Integer> typeId = createNumber("typeId", Integer.class);

    public final StringPath typeName = createString("typeName");

    public QType(String variable) {
        super(Type.class, forVariable(variable));
    }

    public QType(Path<? extends Type> path) {
        super(path.getType(), path.getMetadata());
    }

    public QType(PathMetadata metadata) {
        super(Type.class, metadata);
    }

}

