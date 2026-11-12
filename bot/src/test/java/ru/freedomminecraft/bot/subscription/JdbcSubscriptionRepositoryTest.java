package ru.freedomminecraft.bot.subscription;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ru.freedomminecraft.bot.news.NewsCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcSubscriptionRepositoryTest extends SubscriptionRepositoryContractTest{
    private Connection connection;

    @Override
    protected SubscriptionRepository createRepository() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        return new JdbcSubscriptionsRepository(connection);
    }

    @AfterEach
    void closeConnection() throws SQLException{
        connection.close();
    }

    @Test
    void subscriptionsSurviveReopening(@TempDir Path directory) throws SQLException {
        String url = "jdbc:sqlite:" + directory.resolve("bot.db");

        try (Connection first = DriverManager.getConnection(url)) {
            new JdbcSubscriptionsRepository(first).subscribe(1L, NewsCategory.EVENT);
        }

        try (Connection second = DriverManager.getConnection(url)) {
            SubscriptionRepository reopened = new JdbcSubscriptionsRepository(second);
            assertEquals(Set.of(NewsCategory.EVENT), reopened.categoriesOf(1L));
        }
    }
}
