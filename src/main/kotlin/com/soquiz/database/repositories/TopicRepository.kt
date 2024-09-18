package com.soquiz.database.repositories

import com.soquiz.database.mapping.toTopic
import com.soquiz.database.tables.CategoryDao
import com.soquiz.database.tables.TopicDao
import com.soquiz.database.tables.TopicsTable
import com.soquiz.models.Topic
import com.soquiz.models.TopicWithCategory
import com.soquiz.utils.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class TopicRepository {
    suspend fun allTopics(): List<Topic> = suspendTransaction {
        TopicDao.all().map { it.toTopic() }
    }


    suspend fun allTopicsByCategory(categoryId: String): List<Topic> = suspendTransaction {
        TopicDao.find {
            TopicsTable.category eq categoryId
        }.map { it.toTopic() }
    }


    suspend fun addTopic(topic: TopicWithCategory) = suspendTransaction {
        CategoryDao.findById(topic.categoryId)?.let {
            TopicDao.new {
                topicId = topic.topicId
                category = it
                defaultName = topic.name
            }
        }

    }

    suspend fun updateTopic(topic: Topic) = suspendTransaction {

        val topicDao = TopicDao.findById(topic.topicId)

        topicDao?.apply {
            defaultName = topic.topicName
        }

    }


    suspend fun removeTopic(topicId: String): Boolean = suspendTransaction {
        val rowsDeleted = TopicsTable.deleteWhere {
            TopicsTable.topicId eq topicId
        }
        rowsDeleted == 1
    }

}