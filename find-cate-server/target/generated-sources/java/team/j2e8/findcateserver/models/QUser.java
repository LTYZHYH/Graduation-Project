package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1466674162L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final QAdmin admin;

    public final SetPath<Commity, QCommity> commities = this.<Commity, QCommity>createSet("commities", Commity.class, QCommity.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final SetPath<Reply, QReply> replys = this.<Reply, QReply>createSet("replys", Reply.class, QReply.class, PathInits.DIRECT2);

    public final SetPath<Shop, QShop> shops = this.<Shop, QShop>createSet("shops", Shop.class, QShop.class, PathInits.DIRECT2);

    public final ListPath<TravelStrategy, QTravelStrategy> travelStrategies = this.<TravelStrategy, QTravelStrategy>createList("travelStrategies", TravelStrategy.class, QTravelStrategy.class, PathInits.DIRECT2);

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userName = createString("userName");

    public final StringPath userPassword = createString("userPassword");

    public final StringPath userPhoto = createString("userPhoto");

    public final StringPath userSalt = createString("userSalt");

    public final StringPath userTelenumber = createString("userTelenumber");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
    }

}

