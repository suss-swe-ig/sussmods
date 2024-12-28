package io.swe.api.courses.model

data class CourseInfo(
    val code: String,
    val name: String,
    val level: String,
    val creditUnits: String,
    val language: String,
    val presentationPattern: String,
    val synopsis: String,
    val topics: List<String>,
    val textbooks: List<String>,
    val learningOutcome: List<String>
) {
    class Builder {
        private var code: String = ""
        private var name: String = ""
        private var level: String = ""
        private var creditUnits: String = ""
        private var language: String = ""
        private var presentationPattern: String = ""
        private var synopsis: String = ""
        private var topics: List<String> = emptyList()
        private var textbooks: List<String> = emptyList()
        private var learningOutcome: List<String> = emptyList()

        fun setCode(code: String) = apply { this.code = code }
        fun setName(name: String) = apply { this.name = name }
        fun setLevel(level: String) = apply { this.level = level }
        fun setCreditUnits(creditUnits: String) = apply { this.creditUnits = creditUnits }
        fun setLanguage(language: String) = apply { this.language = language }
        fun setPresentationPattern(presentationPattern: String) = apply { this.presentationPattern = presentationPattern }
        fun setSynopsis(synopsis: String) = apply { this.synopsis = synopsis }
        fun setTopics(topics: List<String>) = apply { this.topics = topics }
        fun setTextbooks(textbooks: List<String>) = apply { this.textbooks = textbooks }
        fun setLearningOutcome(learningOutcome: List<String>) = apply { this.learningOutcome = learningOutcome }

        fun build() = CourseInfo(
            code = code,
            name = name,
            level = level,
            creditUnits = creditUnits,
            language = language,
            presentationPattern = presentationPattern,
            synopsis = synopsis,
            topics = topics,
            textbooks = textbooks,
            learningOutcome = learningOutcome
        )
    }
}