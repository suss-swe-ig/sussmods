package io.swe.api.courses

import io.swe.api.courses.data.CourseInfoService
import io.swe.api.courses.model.CourseInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


@RestController
@RequestMapping("/v1/courses")
class CourseController(
    private val courseInfoProcessor: CourseInfoProcessor,
    private val courseInfoService: CourseInfoService,
) {

    companion object {
        private const val PASSWORD_HASH = "fea477863e4d33008ab7f3ff702ef6b253a44f350c5c189b20d8792dbd9aa785"
    }

    @GetMapping("/info")
    fun getCourseInfo(
        @RequestParam("cursor", required = false) cursor: Long?,
    ): List<CourseInfo> {
        return courseInfoService.pageAll(cursor)
    }

    @PostMapping("/update-all")
    fun getCourses(
        @RequestParam("password") password: String
    ): ResponseEntity<Void> {
        // TODO use spring security
        val hashedPassword = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        if (PASSWORD_HASH != hashedPassword)
            throw IllegalAccessException()

        courseInfoProcessor.updateAll()
        return ResponseEntity.ok().build()
    }


}

