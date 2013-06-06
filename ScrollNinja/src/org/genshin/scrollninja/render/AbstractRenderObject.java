package org.genshin.scrollninja.render;

import org.genshin.engine.system.Disposable;
import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Renderable;
import org.genshin.scrollninja.Global;


/**
 * 描画オブジェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public abstract class AbstractRenderObject implements Renderable, Disposable, PostureInterface
{
	/**
	 * コンストラクタ
	 * @param depth		描画深度（値が大きいものを手前に描画する）
	 */
	public AbstractRenderObject(int depth)
	{
		this.depth = depth;
		
		//---- 描画管理オブジェクトに自身を追加する。
		Global.renderableManager.add(this, this.depth);
	}
	
	@Override
	public void dispose()
	{
		//---- 描画管理オブジェクトから自身を削除する。
		Global.renderableManager.remove(this, depth);
	}

	@Override
	public final void render()
	{
		if(isRenderEnabled())
			localRender();
	}
	
	/**
	 * 描画深度を設定する。
	 * @param depth		描画深度（値が大きいものを手前に描画する）
	 */
	public void setDepth(int depth)
	{
		Global.renderableManager.remove(this, this.depth);
		this.depth = depth;
		Global.renderableManager.add(this, this.depth);
	}
	
	/**
	 * 描画深度を取得する。
	 * @return		描画深度
	 */
	public int getDepth()
	{
		return depth;
	}
	
	/**
	 * 描画フラグを設定する。
	 * @param enabled	描画フラグ
	 */
	public void setRenderEnabled(boolean enabled)
	{
		renderEnabled = enabled;
	}

	/**
	 * 描画フラグを取得する。
	 * @return		描画フラグ
	 */
	public boolean isRenderEnabled()
	{
		return renderEnabled;
	}
	
	/**
	 * オブジェクトを描画する
	 */
	protected abstract void localRender();
	
	
	/** 深度（値が大きいものを手前に描画する） */
	private int depth;
	
	/** 描画フラグ */
	private boolean renderEnabled = true;
}
