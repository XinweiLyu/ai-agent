package com.xinwei.aiagent.constant;


/**
 * 文件相关常量
 */

public interface FileConstant {

    /**
     * 文件保存目录
     */
    // 当前用户目录下的tmp文件夹，如果不存在则创建
    String FILE_SAVE_DIR = System.getProperty("user.dir") + "/tmp";
}
