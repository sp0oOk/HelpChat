package me.spook.project.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.clip.placeholderapi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NullMarked
public class ChatListener implements Listener {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^{}]+)}", Pattern.CASE_INSENSITIVE);
    private static final char COLOR_CHAR = '§';


    public @SuppressWarnings("deprecation") void onChat(AsyncPlayerChatEvent event) {
        final String format = event.getFormat();

        if (!format.contains("{")) {
            event.setFormat(translate(PlaceholderAPI
                    .setPlaceholders(
                            event.getPlayer(),
                            format
                    )));
            return;
        }

        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(format);
        final StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            final String placeholder = matcher.group(1).trim();
            matcher.appendReplacement(result, "%" + placeholder + "%");
        }
        matcher.appendTail(result);

        event.setFormat(translate(
                        PlaceholderAPI
                                .setPlaceholders(
                                        event.getPlayer(),
                                        result.toString()
                                )
                )
        );
    }

    private String translate(@NotNull String message) {
        return translateHexColorCodes("&#", "", message);
    }

    private String translateHexColorCodes(@Nullable String startTag, @Nullable String endTag, @NotNull String message) {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

}
