package io.swe.api.courses

import io.swe.api.support.FileManager
import org.springframework.stereotype.Component

@Component
class CourseInfoDownloader(
    private val courseProps: CourseProps,
    private val fileManager: FileManager
) {

    companion object {
        const val COURSE_PDF_URL =
            "https://sims1.suss.edu.sg/ESERVICE/Public/ViewCourse/ViewCourse.aspx?crsecd=%s&viewtype=pdf&isft=0"
        const val GSP100_PDF_URL =
            "https://sims1.suss.edu.sg/Eservice/Public/ViewCP/ViewCP.aspx?progcd=GSP&viewtype=pdf"
    }

    fun downloadAll(): String {
        val filePath = "${courseProps.destPath}/GSP.pdf"
        fileManager.download(GSP100_PDF_URL, filePath)
        return filePath
    }

    fun download(code: String): String {
        val filePath = "${courseProps.destPath}/$code.pdf"
        fileManager.download(String.format(COURSE_PDF_URL, code), filePath)
        return filePath
    }

}