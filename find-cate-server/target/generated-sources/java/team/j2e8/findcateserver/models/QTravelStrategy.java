package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTravelStrategy is a Querydsl query type for TravelStrategy
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTravelStrategy extends EntityPathBase<TravelStrategy> {

    private static final long serialVersionUID = -278201040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTravelStrategy travelStrategy = new QTravelStrategy("travelStrategy");

    public final StringPath area = createString("area");

    public final QCity city;

    public final SetPath<Commity, QCommity> commity = this.<Commity, QCommity>createSet("commity", Commity.class, QCommity.class, PathInits.DIRECT2);

    public final NumberPath<Integer> favoriteNum = createNumber("favoriteNum", Integer.class);

    public final NumberPath<Integer> isReport = createNumber("isReport", Integer.class);

    public final DateTimePath<java.util.Date> issueTime = createDateTime("issueTime", java.util.Date.class);

    public final StringPath overheadCost = createString("overheadCost");

    public final StringPath reportReason = createString("reportReason");

    public final NumberPath<Integer> scenicNumber = createNumber("scenicNumber", Integer.class);

    public final NumberPath<Integer> strategyAudit = createNumber("strategyAudit", Integer.class);

    public final StringPath strategyContent = createString("strategyContent");

    public final NumberPath<Integer> strategyId = createNumber("strategyId", Integer.class);

    public final StringPath strategyPicture1 = createString("strategyPicture1");

    public final StringPath strategyPicture2 = createString("strategyPicture2");

    public final StringPath strategyPicture3 = createString("strategyPicture3");

    public final StringPath theme = createString("theme");

    public final NumberPath<Integer> travelDays = createNumber("travelDays", Integer.class);

    public final QUser user;

    public QTravelStrategy(String variable) {
        this(TravelStrategy.class, forVariable(variable), INITS);
    }

    public QTravelStrategy(Path<? extends TravelStrategy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTravelStrategy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTravelStrategy(PathMetadata metadata, PathInits inits) {
        this(TravelStrategy.class, metadata, inits);
    }

    public QTravelStrategy(Class<? extends TravelStrategy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.city = inits.isInitialized("city") ? new QCity(forProperty("city")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

