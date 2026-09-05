package com.android.reclaim.util

data class MoodOption(
    val id: String,
    val emoji: String,
    val label: String
)

object MoodOptions {
    val all = listOf(
        MoodOption("excited", "😁", "Excited"),
        MoodOption("happy", "😊", "Happy"),
        MoodOption("content", "🙂", "Content"),
        MoodOption("neutral", "😐", "Neutral"),
        MoodOption("unsure", "😕", "Unsure"),
        MoodOption("sad", "😞", "Sad"),
        MoodOption("crying", "😢", "Crying"),
        MoodOption("overwhelmed", "😭", "Overwhelmed"),
        MoodOption("angry", "😡", "Angry"),
        MoodOption("furious", "🤬", "Furious"),
        MoodOption("tired", "😴", "Tired"),
        MoodOption("sleepy", "🥱", "Sleepy"),
        MoodOption("sick", "🤒", "Sick"),
        MoodOption("nauseous", "🤢", "Nauseous"),
        MoodOption("stressed", "🤯", "Stressed"),
        MoodOption("confident", "😎", "Confident"),
        MoodOption("loved", "🤗", "Loved"),
        MoodOption("tender", "🥺", "Tender"),
        MoodOption("frustrated", "😤", "Frustrated"),
        MoodOption("anxious", "😰", "Anxious")
    )

    fun score(emoji: String): Int {
        return when (emoji) {
            "😁" -> 10
            "😊" -> 9
            "🙂" -> 8
            "😎" -> 8
            "🤗" -> 9
            "🥺" -> 7

            "😐" -> 5
            "😕" -> 4
            "😤" -> 4
            "😰" -> 3

            "😞" -> 3
            "😢" -> 2
            "😭" -> 1
            "😡" -> 2
            "🤬" -> 1

            "😴" -> 4
            "🥱" -> 3
            "🤒" -> 2
            "🤢" -> 2
            "🤯" -> 1

            else -> 5
        }
    }
}
