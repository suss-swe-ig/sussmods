package io.swe.api.courses

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseExtractor
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.RequestCallback

import java.nio.file.Files


import java.nio.file.Paths

@Component
class CourseInfoDownloader(
    private val courseProps: CourseProps,
    private val restTemplate: RestTemplate
) {

    companion object {
        const val PDF_URL =
            "https://sims1.suss.edu.sg/ESERVICE/Public/ViewCourse/ViewCourse.aspx?crsecd=%s&viewtype=pdf&isft=0"
    }

    fun download(code: String) {
        val requestCallback = RequestCallback { request: ClientHttpRequest ->
            request
                .headers.accept = listOf(
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.ALL
            )
        }

        val responseExtractor = ResponseExtractor { response: ClientHttpResponse ->
            val path = Paths.get(courseProps.destPath, "$code.pdf")
            Files.copy(response.body, path)
        }

        restTemplate.execute(String.format(PDF_URL, code), HttpMethod.GET, requestCallback, responseExtractor)
    }

}