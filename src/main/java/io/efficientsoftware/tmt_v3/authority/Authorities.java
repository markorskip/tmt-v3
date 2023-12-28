package io.efficientsoftware.tmt_v3.authority;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Authorities {

    @FieldNameConstants.Include
    ROLE_USER_BASIC,
    @FieldNameConstants.Include
    ROLE_USER_PRO,
    @FieldNameConstants.Include
    ROLE_USER_ADMIN

}
