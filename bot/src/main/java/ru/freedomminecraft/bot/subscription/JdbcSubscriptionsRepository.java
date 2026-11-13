package ru.freedomminecraft.bot.subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import ru.freedomminecraft.bot.news.NewsCategory;
import ru.freedomminecraft.bot.storage.StorageException;

public class JdbcSubscriptionsRepository implements SubscriptionRepository {
    private final Connection connection;

    public JdbcSubscriptionsRepository(Connection connection) {
        this.connection = connection;
        createTableIfMissing();
    }

    private void createTableIfMissing() {
        String sql = """
            CREATE TABLE IF NOT EXISTS subscriptions (
                user_id  BIGINT NOT NULL,
                category TEXT   NOT NULL,
                PRIMARY KEY (user_id, category)
            )
            """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new StorageException("Не удалось создать таблицу подписок", e);
        }
    }

    @Override
    public synchronized void subscribe(long userId, NewsCategory category) {
        String sql = "INSERT INTO subscriptions (user_id, category) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, category.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Не удалось подписать пользователя " + userId, e);
        }
    }

    @Override
    public synchronized void unsubscribe(long userId, NewsCategory category) {
        String sql = "DELETE FROM subscriptions WHERE user_id = ? AND category = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, category.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Не удалось отписать пользователя " + userId, e);
        }
    }

    @Override
    public synchronized Set<NewsCategory> categoriesOf(long userId) {
        String sql = "SELECT category FROM subscriptions WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            Set<NewsCategory> categories = EnumSet.noneOf(NewsCategory.class);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    categories.add(NewsCategory.valueOf(rows.getString("category")));
                }
            }
            return Set.copyOf(categories);
        } catch (SQLException e) {
            throw new StorageException("Не удалось прочитать подписки пользователя " + userId, e);
        }
    }

    @Override
    public Set<Long> subscribersOf(NewsCategory category) {
        String sql = "SELECT user_id FROM subscriptions WHERE category = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.name());
            Set<Long> subscribers = new HashSet<>();
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    subscribers.add(rows.getLong("user_id"));
                }
            }
            return Set.copyOf(subscribers);
        } catch (SQLException e) {
            throw new StorageException("Не удалось прочитать подписчиков категории " + category, e);
        }
    }
}
