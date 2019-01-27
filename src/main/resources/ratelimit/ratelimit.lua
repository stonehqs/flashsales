-- bucket name
local key = KEYS[1]
-- token generate interval
local intervalPerPermit = tonumber(ARGV[1])
-- grant timestamp
local refillTime = tonumber(ARGV[2])
-- limit token count
local limit = tonumber(ARGV[3])
-- ratelimit time period
local interval = tonumber(ARGV[4])

local counter = redis.call('hgetall', key)

if table.getn(counter) == 0 then
    -- first check if bucket not exists, if yes, create a new one with full capacity, then grant access
    redis.call('hmset', key, 'lastRefillTime', refillTime, 'tokensRemaining', limit - 1)
    -- expire will save memory
    redis.call('expire', key, interval)
    return 1
elseif table.getn(counter) == 4 then
    -- if bucket exists, first we try to refill the token bucket
    local lastRefillTime, tokensRemaining = tonumber(counter[2]), tonumber(counter[4])
    local currentTokens
    if refillTime > lastRefillTime then
        -- check if refillTime larger than lastRefillTime.
        -- if not, it means some other operation later than this call made the call first.
        -- there is no need to refill the tokens.
        local intervalSinceLast = refillTime - lastRefillTime
        if intervalSinceLast > interval then
            currentTokens = limit
            redis.call('hset', key, 'lastRefillTime', refillTime)
        else
            local grantedTokens = math.floor(intervalSinceLast / intervalPerPermit)
            if grantedTokens > 0 then
                -- ajust lastRefillTime, we want shift left the refill time.
                local padMillis = math.fmod(intervalSinceLast, intervalPerPermit)
                redis.call('hset', key, 'lastRefillTime', refillTime - padMillis)
            end
            currentTokens = math.min(grantedTokens + tokensRemaining, limit)
        end
    else
        -- if not, it means some other operation later than this call made the call first.
        -- there is no need to refill the tokens.
        currentTokens = tokensRemaining
    end

    assert(currentTokens >= 0)

    if currentTokens == 0 then
        -- we didn't consume any keys
        redis.call('hset', key, 'tokensRemaining', currentTokens)
        return 0
    else
        -- we take 1 token from the bucket
        redis.call('hset', key, 'tokensRemaining', currentTokens - 1)
        return 1
    end
else
    error("Size of counter is " .. table.getn(counter) .. ", Should Be 0 or 4.")
end