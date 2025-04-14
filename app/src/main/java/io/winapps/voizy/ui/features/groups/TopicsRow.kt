package io.winapps.voizy.ui.features.groups

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopicsRow(
    selectedTopic: GroupTopic,
    onTopicSelected: (GroupTopic) -> Unit
) {
    val topicList = listOf(
        GroupTopic.ALL,
        GroupTopic.SCIENCE_AND_TECH,
        GroupTopic.TRAVEL,
        GroupTopic.BUSINESS,
        GroupTopic.EDUCATION,
        GroupTopic.SPORTS_AND_FITNESS,
        GroupTopic.ART,
        GroupTopic.CULTURE,
        GroupTopic.HOBBIES_AND_INTERESTS,
        GroupTopic.RELATIONSHIPS,
        GroupTopic.FOOD_AND_DRINK,
        GroupTopic.CONSPIRACY_THEORIES,
        GroupTopic.POLITICS,
        GroupTopic.HISTORY
    )
    LazyRow(
        modifier = Modifier.padding(4.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(topicList) { topic ->
            Box(
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                TopicButton(
                    topic = topic,
                    selectedTopic = selectedTopic,
                    onTopicSelected = onTopicSelected,
                    label = topic.label
                )
            }
        }
    }
}

@Composable
fun TopicButton(
    topic: GroupTopic,
    selectedTopic: GroupTopic,
    onTopicSelected: (GroupTopic) -> Unit,
    label: String
) {
    val isSelected = (topic == selectedTopic)
    val backgroundColor = if (isSelected) Color(0xFFF10E91) else Color(0xFFFFD5ED)
    val textColor = if (isSelected) Color(0xFFFFD5ED) else Color(0xFFF10E91)

    TextButton(
        onClick = { onTopicSelected(topic) },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, Color(0xFFF10E91))
    ) {
        Text(
            text = label,
            color = textColor
        )
    }
}