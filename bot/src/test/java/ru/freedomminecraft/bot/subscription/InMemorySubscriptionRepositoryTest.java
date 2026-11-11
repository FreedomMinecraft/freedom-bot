package ru.freedomminecraft.bot.subscription;

public class InMemorySubscriptionRepositoryTest extends SubscriptionRepositoryContractTest{
    @Override
    protected SubscriptionRepository createRepository() {
        return new InMemorySubscriptionRepository();
    }
}
