package com.aiit.authority.utils;

public interface RedisPool {

    /**
     * 通过key获取储存在redis中的value
     *
     * @param key
     * @return 成功返回value 失败返回null
     */
    String get(String key);


    /**
     * 向redis存入key和value,并释放连接资源
     * 如果key已经存在 则覆盖
     *
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    String set(String key, String value);

    /**
     * 向redis存入key和value,并释放连接资源
     * 如果key已经存在 则覆盖
     *
     * @param key   key.
     * @param value 值.
     * @param time  秒级时间戳.
     * @return 成功 返回OK 失败返回 0
     */
    String setEx(String key, Integer time, String value);

    /**
     * 向redis删除key,并释放连接资源
     *
     * @param key
     * @return 返回删除成功的个数
     */
    Long del(String key);

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys 一个key 也可以使 string 数组
     * @return 返回删除成功的个数
     */
    Long del(String... keys);


    /**
     * 判断key是否存在
     *
     * @param key
     * @return true OR false
     */
    Boolean exists(String key);


    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     *
     * @param key
     * @param value 过期时间，单位：秒
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    Long expire(String key, Integer value);


    /**
     * 以秒为单位，返回给定 key 的剩余生存时间
     *
     * @param key
     * @return 当 key 不存在时，返回 -2 。当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以秒为单位，返回 key
     * 的剩余生存时间。 发生异常 返回 0
     */
    Long ttl(String key);

}
