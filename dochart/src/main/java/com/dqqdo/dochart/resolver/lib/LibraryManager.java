package com.dqqdo.dochart.resolver.lib;

/**
 * 包管理器
 * 作者：duqingquan
 * 时间：2017/12/5 10:26
 */
public class LibraryManager {

    private volatile static LibraryManager instance;

    private LibraryManager() {

    }

    public static LibraryManager getInstance() {

        if (instance == null) {
            synchronized (LibraryManager.class) {
                if (instance == null) {
                    instance = new LibraryManager();
                }
            }
        }
        return instance;

    }


}
