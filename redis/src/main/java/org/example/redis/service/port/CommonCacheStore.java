package org.example.redis.service.port;

import org.springframework.data.redis.core.SessionCallback;

public interface CommonCacheStore {

    <T> void execute(SessionCallback<T> sessionCallback);
}
