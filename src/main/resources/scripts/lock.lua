local expire = tonumber(ARGV[2])
local ret = redis.call('set', KEYS[1], ARGV[1], 'NX', 'PX', expire)
local strret = tostring(ret)
--用于查看结果，我本机获取锁成功后程序返回随机结果"table: 0x7fb4b3700fe0"，否则返回"false"
redis.call('set', 'result', strret)
if strret == 'false' then
    return false
else
    return true
end