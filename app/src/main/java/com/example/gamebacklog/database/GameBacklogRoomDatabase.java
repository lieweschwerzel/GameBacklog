package com.example.gamebacklog.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.gamebacklog.model.GameBacklog;

@Database(entities = {GameBacklog.class}, version = 1, exportSchema = false)
public abstract class GameBacklogRoomDatabase extends RoomDatabase {
	private final static String NAME_DATABASE = "ga3gmefban_database";

	public abstract GameBacklogDao gameBacklogDao();

	private static volatile GameBacklogRoomDatabase INSTANCE;

	public static GameBacklogRoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (GameBacklogRoomDatabase.class) {
				if (INSTANCE == null) {
					// Create database here
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							GameBacklogRoomDatabase.class, NAME_DATABASE)
							.build();
				}
			}
		}
		return INSTANCE;
	}

}
