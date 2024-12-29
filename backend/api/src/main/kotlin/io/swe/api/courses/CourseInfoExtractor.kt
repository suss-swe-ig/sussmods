package io.swe.api.courses

import io.swe.api.courses.model.CourseInfo
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.StringReader

@Component
class CourseInfoExtractor {

    companion object {
        val sections = listOf(
            "Level:",
            "Credit Units:",
            "Language:",
            "Presentation Pattern:",
            "Synopsis:",
            "Topics:",
            "Textbooks:",
            "Learning Outcome:",
            "Assessment Strategies - "
        )

        val courseCodePattern = Regex("\\b[A-Z]{3}\\d+\\b")
        val sectionPattern = "(${sections.joinToString("|")})\\s*(.*)".toRegex()
        val pagePattern = "Page \\d+ of \\d+".toRegex()
        val isbnRegex = "ISBN[-\\s]?\\d+".toRegex()
    }

    fun extractCode(text: String): Set<String> {
        val courses = mutableSetOf<String>()

        BufferedReader(StringReader(text))
            .lineSequence()
            .forEach { line ->
                val matchResult = courseCodePattern.find(line)
                matchResult?.let { courses.add(it.value) }
            }

        return courses
    }

    fun extractInfo(text: String): CourseInfo {
        val lines = text.split("\n")
        val (courseCode, courseName) = lines[0].split(" ", limit = 2)

        val builder = CourseInfo.Builder()
        builder.setCode(courseCode)
        builder.setName(courseName)

        var section = sections[0]
        val buffer = StringBuilder()

        for (i in 1 until lines.size) {
            val line = lines[i]
            if (pagePattern.containsMatchIn(line)) {
                continue
            }

            val match = sectionPattern.find(line)
            if (match == null) {
                buffer.appendLine(line)
            } else {
                section = section.removeSuffix(":")
                val content = buffer.trim().toString()
                when (section) {
                    "Level" -> builder.setLevel(content)
                    "Credit Units" -> builder.setCreditUnits(content)
                    "Language" -> builder.setLanguage(content)
                    "Presentation Pattern" -> builder.setPresentationPattern(content)
                    "Synopsis" -> builder.setSynopsis(content)
                    "Topics" -> builder.setTopics(content.split("●").map { it.trim() }.filter { it.isNotEmpty() })
                    "Textbooks" -> builder.setTextbooks(splitTextbooksByISBN(content, isbnRegex))
                    "Learning Outcome" ->
                        builder.setLearningOutcome(content.split("●").map { it.trim() }.filter { it.isNotEmpty() })

                    "Assessment Strategies - " -> break
                }
                section = match.groupValues[1]
                buffer.clear()
                buffer.append(match.groupValues[2])
            }
        }

        return builder.build()
    }

    fun splitTextbooksByISBN(content: String, isbnRegex: Regex): List<String> {
        val textbooks = mutableListOf<String>()
        val lines = content.split("\n")
        var currentTextbook = StringBuilder()

        for (line in lines) {
            if (isbnRegex.containsMatchIn(line)) {
                currentTextbook.append("\n").append(line)
                textbooks.add(currentTextbook.toString())
                currentTextbook = StringBuilder()
            } else {
                if (currentTextbook.isNotEmpty()) {
                    currentTextbook.append("\n")
                }
                currentTextbook.append(line)
            }
        }

        if (currentTextbook.isNotEmpty()) {
            textbooks.add(currentTextbook.toString())
        }

        return textbooks
    }
}