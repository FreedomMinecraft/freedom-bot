package ru.freedomminecraft.bot.command.guide;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.freedomminecraft.bot.command.TestContext;
import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaffCommandTest {
    private static class TestStaffCommand extends StaffCommand {
        TestStaffCommand(List<StaffGroup> groups) {
            super(groups);
        }

        @Override
        public String name() {
            return "staff";
        }

        @Override
        public String description() {
            return "Тестовый список";
        }

        @Override
        protected String header() {
            return "ЗАГОЛОВОК";
        }

        @Override
        protected String footer() {
            return "ПОДВАЛ";
        }
    }

    private final StaffCommand staff = new TestStaffCommand(
        List.of(new StaffGroup("Администратор", List.of(
                new TeamMember("pacan1337", List.of()))),
            new StaffGroup("Модераторы", List.of(
                new TeamMember("pacetko228", List.of()),
                new TeamMember("batryxa67", List.of())))));

    @Test
    void showsGroupTitlesInBold(){
        String answer = staff.execute(TestContext.of(""));

        assertTrue(answer.contains("<b>Администратор</b>"));
        assertTrue(answer.contains("<b>Модераторы</b>"));
    }

    @Test
    void listsEveryMember(){
        String answer = staff.execute(TestContext.of(""));

        assertTrue(answer.contains("pacan1337"));
        assertTrue(answer.contains("pacetko228"));
        assertTrue(answer.contains("batryxa67"));
    }

    @Test
    void membersGoUnderTheirGroup(){
        String answer = staff.execute(TestContext.of(""));

        assertTrue(answer.indexOf("Модераторы") < answer.indexOf("pacetko228"));
        assertTrue(answer.indexOf("pacan1337") < answer.indexOf("Модераторы"));
    }

    @Test
    void headerFirstFooterLast(){
        String answer = staff.execute(TestContext.of(""));

        assertTrue(answer.startsWith("ЗАГОЛОВОК"));
        assertTrue(answer.endsWith("ПОДВАЛ"));
    }

    @Test
    void showsMemberLinksAsWords(){
        StaffCommand media = new TestStaffCommand(
            List.of(new StaffGroup("Медиа", List.of(
                new TeamMember("apollodance", List.of(
                    new Link("Twitch", "https://www.twitch.tv/test"),
                    new Link("Telegram", "https://t.me/test")))))));

        String answer = media.execute(TestContext.of(""));

        assertTrue(answer.contains("apollodance — "
            + Html.link("Twitch", "https://www.twitch.tv/test") + " · "
            + Html.link("Telegram", "https://t.me/test")));
    }

    @Test
    void memberWithoutLinksHasNoDash(){
        String answer = staff.execute(TestContext.of(""));

        assertFalse(answer.contains("pacan1337 —"));
    }

    @Test
    void escapesGroupAndMemberData(){
        StaffCommand escaped = new TestStaffCommand(
            List.of(new StaffGroup("Модераторы & хелперы", List.of(
                new TeamMember("<Ник>", List.of())))));

        String answer = escaped.execute(TestContext.of(""));

        assertTrue(answer.contains("Модераторы &amp; хелперы"));
        assertTrue(answer.contains("&lt;Ник&gt;"));
    }

    @Test
    void emptyListSaysSo(){
        StaffCommand empty = new TestStaffCommand(List.of());

        assertTrue(empty.execute(TestContext.of("")).contains("пуст"));
    }
}
