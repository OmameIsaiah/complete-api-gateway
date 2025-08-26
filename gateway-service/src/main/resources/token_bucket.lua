-- KEYS[1] = bucket key (hash)
-- ARGV[1] = capacity (max tokens)
-- ARGV[2] = refill_tokens
-- ARGV[3] = refill_interval_millis
-- ARGV[4] = now_millis
-- ARGV[5] = expire_seconds

local bucketKey = KEYS[1]
local capacity = tonumber(ARGV[1])
local refillTokens = tonumber(ARGV[2])
local refillIntervalMillis = tonumber(ARGV[3])
local now = tonumber(ARGV[4])
local expireSeconds = tonumber(ARGV[5])

local h = redis.call('HGETALL', bucketKey)
local tokens = capacity
local lastRefill = now

if next(h) ~= nil then
  for i = 1, #h, 2 do
    if h[i] == 'tokens' then tokens = tonumber(h[i+1]) end
    if h[i] == 'lastRefillTs' then lastRefill = tonumber(h[i+1]) end
  end

  if now > lastRefill then
    local elapsed = now - lastRefill
    local intervals = math.floor(elapsed / refillIntervalMillis)
    if intervals > 0 then
      local newTokens = tokens + (intervals * refillTokens)
      if newTokens > capacity then newTokens = capacity end
      tokens = newTokens
      lastRefill = lastRefill + (intervals * refillIntervalMillis)
    end
  end
end

if tokens >= 1 then
  tokens = tokens - 1
  redis.call('HMSET', bucketKey, 'tokens', tokens, 'lastRefillTs', lastRefill)
  redis.call('EXPIRE', bucketKey, expireSeconds)
  return 1
else
  -- no token available
  redis.call('HMSET', bucketKey, 'tokens', tokens, 'lastRefillTs', lastRefill)
  redis.call('EXPIRE', bucketKey, expireSeconds)
  return 0
end
