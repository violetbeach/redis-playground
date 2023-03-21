package com.violetbeach.redisplayground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RankingService {

    private static final String LEADERBOARD_KEY = "leaderBoard";

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean setUserScore(String userId, int score) {
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        zSetOps.add(LEADERBOARD_KEY, userId, score);

        return true;
    }

    public Long getUserRanking(String userId) {
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        return zSetOps.rank(LEADERBOARD_KEY, userId);
    }

    public Set<String> getTopRank(int limit) {
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        return zSetOps.reverseRange(LEADERBOARD_KEY, 0, limit - 1);
    }


}
