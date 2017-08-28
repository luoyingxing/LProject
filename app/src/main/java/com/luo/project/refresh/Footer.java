package com.luo.project.refresh;


/**
 * Footer
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public interface Footer {

    /**
     * 手指拖动下拉（会连续多次调用）
     *
     * @param percent      下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+extendHeight) / footerHeight )
     * @param offset       下拉的像素偏移量  0 - offset - (footerHeight+extendHeight)
     * @param footerHeight Footer的高度
     * @param extendHeight Footer的扩展高度
     */
    void onPullingUp(float percent, int offset, int footerHeight, int extendHeight);

    /**
     * 手指释放之后的持续动画（会连续多次调用）
     *
     * @param percent      下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+extendHeight) / footerHeight )
     * @param offset       下拉的像素偏移量  0 - offset - (footerHeight+extendHeight)
     * @param footerHeight Footer的高度
     * @param extendHeight Footer的扩展高度
     */
    void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight);

}