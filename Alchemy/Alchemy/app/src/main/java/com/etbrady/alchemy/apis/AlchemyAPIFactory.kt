package com.etbrady.alchemy.apis

import android.content.Context
import com.etbrady.alchemy.R
import retrofit2.Retrofit

class AlchemyAPIFactory {
    companion object {
        fun createAlchemyAPIInstance(context: Context): AlchemyAPI {
            val retrofit = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.alchemy_base_url))
                    .build()

            return retrofit.create(AlchemyAPI::class.java)
        }
    }
}