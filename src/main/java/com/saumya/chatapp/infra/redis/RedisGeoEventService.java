package com.saumya.chatapp.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisGeoEventService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String EVENT_KEY = "events";
    private static final double DEFAULT_RADIUS_KM = 50.0;

    public void addEvent(
            Long eventId,
            double longitude,
            double latitude
    ) {

        redisTemplate.opsForGeo().add(
                EVENT_KEY,
                new Point(longitude, latitude),
                eventId.toString()
        );
    }

    public void removeEvent(Long eventId){
        redisTemplate.opsForGeo().remove(
                EVENT_KEY,
                eventId.toString()
        );
    }

    public List<Long> getNearbyEventIds(
            double longitude,
            double latitude
    ) {

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo()
                        .search(
                                EVENT_KEY,
                                GeoReference.fromCoordinate(
                                        longitude,
                                        latitude
                                ),
                                new Distance(
                                        DEFAULT_RADIUS_KM,
                                        Metrics.KILOMETERS
                                ),
                                RedisGeoCommands.GeoSearchCommandArgs
                                        .newGeoSearchArgs()
                                        .includeDistance()
                                        .sortAscending()
                                        .limit(100)
                        );

        if(results == null) return List.of();

        return results.getContent()
                .stream()
                .map(x -> Long.parseLong(
                        x.getContent().getName()
                ))
                .toList();
    }
}