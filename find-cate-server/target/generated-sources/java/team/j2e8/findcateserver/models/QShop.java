package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = -1466744007L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShop shop = new QShop("shop");

    public final SetPath<Commity, QCommity> commities = this.<Commity, QCommity>createSet("commities", Commity.class, QCommity.class, PathInits.DIRECT2);

    public final SetPath<Food, QFood> foods = this.<Food, QFood>createSet("foods", Food.class, QFood.class, PathInits.DIRECT2);

    public final NumberPath<Integer> shopActive = createNumber("shopActive", Integer.class);

    public final StringPath shopAddress = createString("shopAddress");

    public final NumberPath<Integer> shopDislike = createNumber("shopDislike", Integer.class);

    public final NumberPath<Integer> shopId = createNumber("shopId", Integer.class);

    public final NumberPath<Double> shopLat = createNumber("shopLat", Double.class);

    public final NumberPath<Integer> shopLike = createNumber("shopLike", Integer.class);

    public final NumberPath<Double> shopLng = createNumber("shopLng", Double.class);

    public final StringPath shopName = createString("shopName");

    public final StringPath shopPhoto = createString("shopPhoto");

    public final StringPath shopTelenumber = createString("shopTelenumber");

    public final SetPath<Type, QType> types = this.<Type, QType>createSet("types", Type.class, QType.class, PathInits.DIRECT2);

    public final QUser user;

    public QShop(String variable) {
        this(Shop.class, forVariable(variable), INITS);
    }

    public QShop(Path<? extends Shop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShop(PathMetadata metadata, PathInits inits) {
        this(Shop.class, metadata, inits);
    }

    public QShop(Class<? extends Shop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

