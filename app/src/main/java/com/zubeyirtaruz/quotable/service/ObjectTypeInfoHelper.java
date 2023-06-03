package com.zubeyirtaruz.quotable.service;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.ObjectTypeInfo;
import com.zubeyirtaruz.quotable.model.FavoriteQuote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ObjectTypeInfoHelper {
    private static final int FORMAT_VERSION = 2;
    private static final int OBJECT_TYPE_VERSION = 19;

    public static ObjectTypeInfo getObjectTypeInfo() {
        ObjectTypeInfo objectTypeInfo = new ObjectTypeInfo();
        objectTypeInfo.setFormatVersion(FORMAT_VERSION);
        objectTypeInfo.setObjectTypeVersion(OBJECT_TYPE_VERSION);
        List<Class<? extends CloudDBZoneObject>> objectTypeList = new ArrayList<>();
        Collections.addAll(objectTypeList, FavoriteQuote.class);
        objectTypeInfo.setObjectTypes(objectTypeList);
        return objectTypeInfo;
    }
}
