package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFood is a Querydsl query type for Food
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFood extends EntityPathBase<Food> {

    private static final long serialVersionUID = -1467124575L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFood food = new QFood("food");

    public final QArea area;

    public final NumberPath<Integer> food_dislike = createNumber("food_dislike", Integer.class);

    public final NumberPath<Integer> food_like = createNumber("food_like", Integer.class);

    public final NumberPath<Integer> foodId = createNumber("foodId", Integer.class);

    public final StringPath foodIntroduction = createString("foodIntroduction");

    public final StringPath foodName = createString("foodName");

    public final StringPath foodPhoto = createString("foodPhoto");

    public final NumberPath<Float> foodPrice = createNumber("foodPrice", Float.class);

    public final QShop shop;

    public final QType type;

    public QFood(String variable) {
        this(Food.class, forVariable(variable), INITS);
    }

    public QFood(Path<? extends Food> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFood(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFood(PathMetadata metadata, PathInits inits) {
        this(Food.class, metadata, inits);
    }

    public QFood(Class<? extends Food> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.area = inits.isInitialized("area") ? new QArea(forProperty("area"), inits.get("area")) : null;
        this.shop = inits.isInitialized("shop") ? new QShop(forProperty("shop"), inits.get("shop")) : null;
        this.type = inits.isInitialized("type") ? new QType(forProperty("type")) : null;
    }

}

