package com.arctouch.codechallenge.util

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

fun buildResponse() : Response<Error> {
    return Response.error(403, ResponseBody.create(MediaType.parse("application/json"), ""))
}
