package com.example.myapplication.model

data class Root(
    val candidates: List<Candidate>,
)

data class Candidate(
    val content: Content,
    val finishReason: String,
    val index: Long,
    val safetyRatings: List<SafetyRating>,
)
data class BardRequest(
    val contents: List<Content>
)
data class Content(
    val parts: List<Part>,

)

data class Part(
    val text: String,
)

data class SafetyRating(
    val category: String,
    val probability: String,
)