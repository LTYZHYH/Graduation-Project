package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommity is a Querydsl query type for Commity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCommity extends EntityPathBase<Commity> {

    private static final long serialVersionUID = 109972223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommity commity = new QCommity("commity");

    public final StringPath commityContent = createString("commityContent");

    public final NumberPath<Integer> commityDislike = createNumber("commityDislike", Integer.class);

    public final NumberPath<Integer> commityId = createNumber("commityId", Integer.class);

    public final NumberPath<Integer> commityLike = createNumber("commityLike", Integer.class);

    public final DateTimePath<java.util.Date> commityTime = createDateTime("commityTime", java.util.Date.class);

    public final NumberPath<Integer> isReport = createNumber("isReport", Integer.class);

    public final ListPath<Reply, QReply> replyList = this.<Reply, QReply>createList("replyList", Reply.class, QReply.class, PathInits.DIRECT2);

    public final QShop shop;

    public final QTravelStrategy travelStrategy;

    public final QUser user;

    public QCommity(String variable) {
        this(Commity.class, forVariable(variable), INITS);
    }

    public QCommity(Path<? extends Commity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommity(PathMetadata metadata, PathInits inits) {
        this(Commity.class, metadata, inits);
    }

    public QCommity(Class<? extends Commity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shop = inits.isInitialized("shop") ? new QShop(forProperty("shop"), inits.get("shop")) : null;
        this.travelStrategy = inits.isInitialized("travelStrategy") ? new QTravelStrategy(forProperty("travelStrategy"), inits.get("travelStrategy")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

