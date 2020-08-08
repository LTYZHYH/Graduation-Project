package team.j2e8.findcateserver.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArea is a Querydsl query type for Area
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArea extends EntityPathBase<Area> {

    private static final long serialVersionUID = -1467270960L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArea area = new QArea("area");

    public final NumberPath<Integer> areaId = createNumber("areaId", Integer.class);

    public final StringPath areaName = createString("areaName");

    public final QCity city;

    public QArea(String variable) {
        this(Area.class, forVariable(variable), INITS);
    }

    public QArea(Path<? extends Area> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArea(PathMetadata metadata, PathInits inits) {
        this(Area.class, metadata, inits);
    }

    public QArea(Class<? extends Area> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.city = inits.isInitialized("city") ? new QCity(forProperty("city")) : null;
    }

}

