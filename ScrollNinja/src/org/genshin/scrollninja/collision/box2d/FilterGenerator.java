package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.Filter;

/**
 * 衝突判定のFilter生成まっしん
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class FilterGenerator
{
	/**
	 * Filterを生成する
	 * @param outFilter		出力先のFilter
	 */
	public void generate(Filter outFilter)
	{
		outFilter.categoryBits = CategoryBitsFactory.getInstance().get(category);
		
		outFilter.maskBits = -1;
		for(String ignoreCategory : ignoreCategories)
		{
			outFilter.maskBits &= ~CategoryBitsFactory.getInstance().get(ignoreCategory);
		}
	}
	
	
	/** 自身のカテゴリ名 */
	public String category;
	
	/** 衝突判定を取らないカテゴリ名の配列 */
	public String[] ignoreCategories;
}
