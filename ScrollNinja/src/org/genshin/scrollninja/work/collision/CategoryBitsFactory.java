package org.genshin.scrollninja.work.collision;

import org.genshin.engine.system.factory.AbstractFlyweightFactory;

/**
 * 衝突判定のカテゴリを表すビットマスクを生成するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class CategoryBitsFactory extends AbstractFlyweightFactory<String, Short>
{
	/**
	 * コンストラクタ
	 */
	private CategoryBitsFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	static CategoryBitsFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected Short create(String key)
	{
		final int size = size();
		assert size < 16 : "衝突判定のカテゴリは16種類まで。(size = " + size + ")";
		return (short)(1 << size);
	}
	
	/** シングルトンインスタンス */
	private final static CategoryBitsFactory instance = new CategoryBitsFactory();
}
