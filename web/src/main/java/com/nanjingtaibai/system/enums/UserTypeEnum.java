package com.nanjingtaibai.system.enums;

public enum UserTypeEnum {
    STEM_ADMIN(0),
    STEM_USER(1)
    ;
    private  int typeCode;

    UserTypeEnum(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }
}
