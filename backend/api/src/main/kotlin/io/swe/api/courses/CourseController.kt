package io.swe.api.courses

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/courses")
class CourseController {
    @GetMapping("/hello")
    fun getCourses(): ResponseEntity<String> {
        return ResponseEntity.ok("hello from api");
    }


}