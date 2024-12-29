package io.swe.api.courses

import io.swe.api.courses.data.CourseInfoService
import io.swe.api.support.FileManager
import io.swe.api.support.PDFTextStripper
import org.springframework.stereotype.Component

@Component
class CourseInfoProcessor(
    private val courseInfoDownloader: CourseInfoDownloader,
    private val courseInfoExtractor: CourseInfoExtractor,
    private val courseInfoService: CourseInfoService,
    private val pdfTextStripper: PDFTextStripper,
    private val fileManager: FileManager
) {

    fun updateAll() {
        val resource = CourseInfoDownloader::class.java.classLoader.getResource("program/GSP.pdf")
        if (resource != null) {
            val progText = pdfTextStripper.getText(resource.path)
            val codes = courseInfoExtractor.extractCode(progText)

            for (code in codes) {
                try {
                    val courseFilePath = courseInfoDownloader.download(code)
                    val courseText = pdfTextStripper.getText(courseFilePath)
                    val courseInfo = courseInfoExtractor.extractInfo(courseText)
                    fileManager.delete(courseFilePath)

                    courseInfoService.upsert(courseInfo)
                } catch (e: Exception) {
                    println("failed to download course: $code, error: ${e.message}")
                }
            }
        }
    }

}