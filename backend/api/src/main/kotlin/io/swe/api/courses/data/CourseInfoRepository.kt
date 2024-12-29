package io.swe.api.courses.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.swe.api.courses.model.CourseInfo
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CourseInfoRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    fun pageAll(cursor: Long? = null): List<CourseInfo> {
        val sql = """
            SELECT * 
            FROM course_info
            WHERE id > ?
            ORDER BY id
            LIMIT 10
        """

        return jdbcTemplate.query(
            sql,
            { rs, _ ->
                CourseInfo(
                    id = rs.getLong("id"),
                    code = rs.getString("code"),
                    name = rs.getString("name"),
                    level = rs.getString("level"),
                    creditUnits = rs.getString("credit_units"),
                    language = rs.getString("language"),
                    presentationPattern = rs.getString("presentation_pattern"),
                    synopsis = rs.getString("synopsis"),
                    topics = objectMapper.readValue(rs.getString("topics")),
                    textbooks = objectMapper.readValue(rs.getString("textbooks")),
                    learningOutcome = objectMapper.readValue(rs.getString("learning_outcome"))
                )
            },
            cursor ?: 0L
        )
    }

    fun upsert(courseInfo: CourseInfo) {
        val sql = """
            INSERT INTO course_info (
                code, name, level, credit_units, language, presentation_pattern, 
                synopsis, topics, textbooks, learning_outcome
            ) VALUES (
                ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?::jsonb, ?::jsonb
            )
            ON CONFLICT (code) DO UPDATE SET
                name = EXCLUDED.name,
                level = EXCLUDED.level,
                credit_units = EXCLUDED.credit_units,
                language = EXCLUDED.language,
                presentation_pattern = EXCLUDED.presentation_pattern,
                synopsis = EXCLUDED.synopsis,
                topics = EXCLUDED.topics,
                textbooks = EXCLUDED.textbooks,
                learning_outcome = EXCLUDED.learning_outcome
        """

        jdbcTemplate.update(
            sql,
            courseInfo.code,
            courseInfo.name,
            courseInfo.level,
            courseInfo.creditUnits,
            courseInfo.language,
            courseInfo.presentationPattern,
            courseInfo.synopsis,
            objectMapper.writeValueAsString(courseInfo.topics),
            objectMapper.writeValueAsString(courseInfo.textbooks),
            objectMapper.writeValueAsString(courseInfo.learningOutcome)
        )
    }
}
