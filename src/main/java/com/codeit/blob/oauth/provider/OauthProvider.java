package com.codeit.blob.oauth.provider;

import com.codeit.blob.oauth.OauthType;
import io.jsonwebtoken.lang.Assert;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OauthProvider {
    private final EnumMap<OauthType, OauthProperties> enumMap = new EnumMap<>(OauthType.class);

    public OauthProvider(Set<OauthProperties> providers) {
        Assert.notEmpty(providers, "oauth provider must not empty");
        providers.forEach(provider -> enumMap.put(provider.getOauthType(), provider));
    }

    public OauthProperties getProperties(@NonNull OauthType oauthType) {
        Assert.isTrue(enumMap.containsKey(oauthType), "oauth provider must not null");
        return enumMap.get(oauthType);
    }
}
