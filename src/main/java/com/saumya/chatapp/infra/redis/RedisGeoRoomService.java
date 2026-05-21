package com.saumya.chatapp.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisGeoRoomService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String ROOM_KEY = "room";
    private static final double DEFAULT_RADIUS_KM = 20.0;

    public void addRoom(
            String roomId,
            double longitude,
            double latitude
    ){
        redisTemplate.opsForGeo().add(
                ROOM_KEY,
                new Point(longitude, latitude),
                roomId
        );
    }

    public void removeRoom(String roomId){
        redisTemplate.opsForGeo().remove(
                ROOM_KEY,
                roomId
        );
    }

    public List<String> getNearbyRoomIds(
            double longitude,
            double latitude
    ){
        GeoResults<RedisGeoCommands.GeoLocation<String>> results=
                redisTemplate.opsForGeo()
                        .search(
                                ROOM_KEY,
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

        if(results==null) return List.of();

        return results.getContent()
                .stream()
                .map(x->(
                        x.getContent().getName()
                ))
                .toList();
    }
}
