package ru.freedomminecraft.bot.command;

import java.util.List;

import ru.freedomminecraft.bot.format.Html;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.TeamMember;
import ru.freedomminecraft.bot.serverinfo.StaffGroup;

public class StaffCommand implements Command{
    private final String name;
    private final String description;
    private final String title;
    private final List<StaffGroup> groups;

    public StaffCommand(String name, String description, String title, List<StaffGroup> groups){
        this.name = name;
        this.description = description;
        this.title = title;
        this.groups = List.copyOf(groups);
    }

    @Override
    public String name(){
        return name;
    }

    @Override
    public String description(){
        return description;
    }

    @Override
    public String execute(String argument){
        if (groups.isEmpty()) {
            return title + "\nСписок пока пуст.";
        }
        StringBuilder text = new StringBuilder(title).append("\n");
        for (StaffGroup group : groups){
            text.append("\n")
                .append(Html.escape(group.title()))
                .append(":\n");
            for(TeamMember member : group.members()){
                text.append("• ")
                    .append(Html.escape(member.nickname()))
                    .append("\n");
                for (Link link : member.links()) {
                    text.append("    ")
                        .append(Html.escape(link.title()))
                        .append(": ")
                        .append(Html.escape(link.url()))
                        .append("\n");
                }
            }
        }
        return text.toString();
    }
}
