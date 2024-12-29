package io.swe.api.support

import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.stereotype.Component
import java.io.File

@Component
class PDFTextStripper {

    fun getText(filePath: String): String {
        return Loader
            .loadPDF(File(filePath))
            .use { document ->
                PDFTextStripper().getText(document)
            }
    }

}