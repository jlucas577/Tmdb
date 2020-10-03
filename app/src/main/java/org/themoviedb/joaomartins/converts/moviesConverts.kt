package org.themoviedb.joaomartins.converts

import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class Results<T> {
    var results: T? = null
}

class DataConverter<Any>(
    private val delegate: Converter<ResponseBody, Results<Any>>?
) : Converter<ResponseBody, Any> {
    override fun convert(value: ResponseBody): Any? {
        val graphQLDataModel = delegate?.convert(value)
        return graphQLDataModel?.results
    }
}

class DataConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return try {
            val dataType = TypeToken.getParameterized(Results::class.java, type).type
            val converter: Converter<ResponseBody, Results<Any>>? = retrofit.nextResponseBodyConverter(this, dataType, annotations)
            DataConverter(converter)
        } catch (e: Exception) {
            null
        }
    }
}