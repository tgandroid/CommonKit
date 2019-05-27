package com.ssf.commonkt.di.module

import com.ssf.commonkt.data.network.IConstantPool
import com.ssf.commonkt.data.network.IOfficialApi
import com.ssf.framework.net.common.RetrofitClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @atuthor ydm
 * @data on 2018/8/13
 * @describe
 */
@Module
internal object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun getHttpService(): IOfficialApi {
        return RetrofitClient.Builder(
                IOfficialApi::class.java,
                true,
                IConstantPool.sCommonUrl,
                headers = { HashMap() }
        ).create()
    }

}