package io.github.catomon.yutaka.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(entities = [PostEntity::class], version = 3, exportSchema = false)
@TypeConverters(StringListConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<PostDatabase> {
    override fun initialize(): PostDatabase
}
