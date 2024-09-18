package com.soquiz.database.mapping

import com.soquiz.database.tables.TopicDao
import com.soquiz.models.Topic


fun TopicDao.toTopic() = Topic(
    topicId = this.topicId,
    topicName = this.defaultName
)


