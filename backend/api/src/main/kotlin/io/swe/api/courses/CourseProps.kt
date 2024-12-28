package io.swe.api.courses

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CourseProps(
    @Value("\${io.swe.api.dest-path}") val destPath: String,
)