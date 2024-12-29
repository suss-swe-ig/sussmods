package io.swe.api.courses.data

import io.swe.api.courses.model.CourseInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseInfoService(
    private val repository: CourseInfoRepository
) {

    fun pageAll(
        cursor: Long? = null,
        code: String? = null,
        name: String? = null
    ): List<CourseInfo> {
        return repository.pageAll(cursor, code, name)
    }

    @Transactional
    fun upsert(courseInfo: CourseInfo) {
        repository.upsert(courseInfo)
    }
}