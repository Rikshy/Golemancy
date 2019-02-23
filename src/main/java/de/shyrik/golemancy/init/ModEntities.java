package de.shyrik.golemancy.init;

import de.shyrik.golemancy.entity.golem.EntityModGolem;
import net.minecraft.entity.EntityType;

public class ModEntities {
    public static EntityType GOLEM = EntityType.register(
            EntityModGolem.GOLEM.toString(),
            EntityType.Builder.create(EntityModGolem.class, EntityModGolem::new));
}
