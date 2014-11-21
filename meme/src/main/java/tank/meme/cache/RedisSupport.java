package tank.meme.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * redis的支持类,使用jedis类库来操作redis
 * 
 * @ClassName: RedisSupport
 * @Description: TODO
 * @author tank
 * @date 2014-10-15 下午3:07:23
 */
public class RedisSupport {

	private static RedisSupport context;
	private static Logger logger = LoggerFactory.getLogger(RedisSupport.class);

	private static JedisSentinelPool pool;
	// 加载配置信息
	static {

		Properties properties = new Properties();
		InputStream in = RedisSupport.class.getResourceAsStream("/redis.properties");

		try {
			properties.load(in);
			logger.info("{}", properties.getProperty("maxTotal"));
			Integer timeout = Integer.parseInt(properties.getProperty("timeout"));
			Integer maxTotal = Integer.parseInt(properties.getProperty("maxTotal"));
			Integer maxIdle = Integer.parseInt(properties.getProperty("maxIdle"));
			Integer maxWaitMillis = Integer.parseInt(properties.getProperty("maxWaitMillis"));
			Boolean testOnBorrow = Boolean.parseBoolean(properties.getProperty("testOnBorrow"));
			Boolean testOnReturn = Boolean.parseBoolean(properties.getProperty("testOnReturn"));

			String master = properties.getProperty("master");
			String sentinelStr = properties.getProperty("sentinel");

			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(maxIdle);
			poolConfig.setMaxTotal(maxTotal);
			poolConfig.setMaxWaitMillis(maxWaitMillis);
			poolConfig.setTestOnBorrow(testOnBorrow);
			poolConfig.setTestOnReturn(testOnReturn);

			Set<String> sentinels = new HashSet<String>();
			String[] sentinelArray = sentinelStr.split(",");
			for (String sentinel : sentinelArray) {
				sentinels.add(sentinel);
			}

			pool = new JedisSentinelPool(master, sentinels, poolConfig, timeout);// timeout
																					// 读取超时

		} catch (IOException e) {
			logger.error("初始化权限数据:{}", e);
		}

	}

	private RedisSupport() {

	}

	public static RedisSupport getInstance() {
		if (context == null) {
			synchronized (RedisSupport.class) {
				if (context == null) {
					context = new RedisSupport();
				}
			}
		}
		return context;
	}

	public Jedis getJedis() {
		return pool.getResource();
	}

	public void returnResource(Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	public void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			pool.returnBrokenResource(jedis);
		}
	}

	public void set(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public void lpush(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void lpushRtbLog(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.lpush(key, value);
			jedis.lpush(key + "_log", value);
			jedis.ltrim(key + "_log", 0, 19999);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void rpush(String key, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.rpush(key, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public List<String> blpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.blpop(0, key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public List<String> brpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.brpop(0, key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String rpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long lpush(String key, String... values) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpush(key, values);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String lpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	/**
	 * 判断某个对象是否在集合中，适用于做i c a 去重
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	/**
	 * 往集合中增加一个对象
	 * 
	 * @param key
	 * @param members
	 */
	public void sadd(String key, String members) {
		Jedis jedis = getJedis();
		try {
			jedis.sadd(key, members);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * hash表field's value增操作，增值为long类型，适用于增加次数
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hincrBy(String key, String field, long value) {
		Jedis jedis = getJedis();
		try {
			jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * hash表field's value增操作，增值为long类型，适用于增加花费数
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hincrByFloat(String key, String field, double value) {
		Jedis jedis = getJedis();
		try {
			jedis.hincrByFloat(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void zincrby(String key, double score, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.zincrby(key, score, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public void sadd(String key, String... values) {
		Jedis jedis = getJedis();
		try {
			jedis.sadd(key, values);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public long sadd(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return 0;
	}

	public Set<String> smembers(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public void hset(String key, String field, String value) {
		Jedis jedis = getJedis();
		try {
			jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = getJedis();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			logger.error("{}", e);
			returnBrokenResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

}
