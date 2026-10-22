package ru.freedomminecraft.bot.command;

import java.util.List;
import ru.freedomminecraft.bot.serverinfo.Link;
import ru.freedomminecraft.bot.serverinfo.TeamMember;

public class StaffCommand implements Command{
    private final String name;
    private final String description;
    private final String title;
    private final List<TeamMember> members;

    public StaffCommand(String name, String description, String title, List<TeamMember> members){
        this.name = name;
        this.description = description;
        this.title = title;
        this.members = List.copyOf(members);
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
        if (members.isEmpty()) {
            return title + "\nСписок пока пуст.";
        }
        StringBuilder text = new StringBuilder(title).append("\n");
        for (TeamMember member : members){
            text.append("• ")
                .append(member.nickname())
                .append(" — ")
                .append(member.position())
                .append("\n");
            for(Link link : member.links()){
                text.append("    ")
                    .append(link.title())
                    .append(": ")
                    .append(link.url())
                    .append("\n");
            }
        }
        return text.toString();
    }
}
