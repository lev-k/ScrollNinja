package org.genshin.engine.system.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * オブジェクトを生成する基本クラス。<br>
 * オブジェクトを取得する際、使い回せるものがあればそれを使い回す。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <K>	オブジェクトを判別するための識別子
 * @param <V>	生成するオブジェクトの型
 */
public abstract class AbstractFlyweightFactory<K, V> implements FactoryInterface<K, V>
{
	@Override
	public final V get(K key)
	{
		V result = objects.get(key);
		if(result == null)
		{
			result = create(key);
			objects.put(key, result);
		}
		return result;
	}

	/**
	 * 新しいオブジェクトを生成する。
	 * @param key	オブジェクトの識別子
	 * @return		新しいオブジェクト
	 */
	protected abstract V create(K key);
	
	/** 管理オブジェクトのマップ */
	private final Map<K, V> objects = new HashMap<K, V>();
}
