package io.swe.api.support

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Component
class FileManager(private val restTemplate: RestTemplate) {

    fun download(url: String, destPath: String) {
        val destFile = File(destPath)
        val parentDir = destFile.parentFile
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }

        if (destFile.exists() && destFile.isFile) {
            destFile.delete()
        }

        restTemplate.execute(
            url,
            HttpMethod.GET,
            { request ->
                request.headers.accept = listOf(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL)
            },
            { response ->
                response.body.use { inputStream -> Files.copy(inputStream, Paths.get(destPath)) }
            }
        ) ?: throw RuntimeException("Failed to download file from $url")
    }

    fun delete(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }


}