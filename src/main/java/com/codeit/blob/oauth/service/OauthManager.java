package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import io.jsonwebtoken.lang.Assert;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Set;

@Component
public class OauthManager {
    private final EnumMap<OauthType, OauthService> enumMap;

    @Autowired
    public OauthManager(Set<OauthService> services) {
        enumMap = new EnumMap<>(OauthType.class);
        Assert.notEmpty(services, "oauth provider must not empty");
        services.forEach(service -> enumMap.put(service.oauthType, service));
    }

    public OauthService getService(@NonNull OauthType oauthType) {
        Assert.isTrue(enumMap.containsKey(oauthType), "oauth provider must not null");
        return enumMap.get(oauthType);
    }
}
