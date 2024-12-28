package io.swe.api.courses

import io.swe.api.courses.model.CourseInfo
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


@RestController
@RequestMapping("/v1/courses")
class CourseController(
    private val courseInfoExtractor: CourseInfoExtractor,
    private val courseInfoDownloader: CourseInfoDownloader,
    private val courseProps: CourseProps,
) {

    companion object {
        private const val PASSWORD_HASH = "fea477863e4d33008ab7f3ff702ef6b253a44f350c5c189b20d8792dbd9aa785"
    }

    @GetMapping("/info")
    fun getCourseInfo(
        @RequestParam("code") code: String,
        @RequestParam("password") password: String
    ): CourseInfo {
        // TODO use spring security
        val hashedPassword = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        if (PASSWORD_HASH != hashedPassword)
            throw IllegalAccessException()

        val localFilePath = "${courseProps.destPath}/$code.pdf"

        val document: PDDocument? = Loader.loadPDF(File(localFilePath))
        val pdfText = PDFTextStripper().getText(document)
        document?.close()

        val courseInfo = courseInfoExtractor.extractInfo(pdfText)

        return courseInfo
    }

    @GetMapping("/download")
    fun getCourses(
        @RequestParam("code") code: String,
    ): ResponseEntity<Void> {
        courseInfoDownloader.download("ABS103")
        return ResponseEntity.ok().build()
    }


}

