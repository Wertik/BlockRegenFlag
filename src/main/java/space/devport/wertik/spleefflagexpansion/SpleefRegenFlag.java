package space.devport.wertik.spleefflagexpansion;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import org.jetbrains.annotations.Nullable;

public class SpleefRegenFlag extends Flag<String> {

    protected SpleefRegenFlag(String name) {
        super(name);
    }

    protected SpleefRegenFlag(String name, @Nullable RegionGroup defaultGroup) {
        super(name, defaultGroup);
    }

    @Override
    public String parseInput(FlagContext context) throws InvalidFlagFormat {
        String input = context.getUserInput().trim();
        SpleefFlagPlugin.getInstance().getConsoleOutput().debug("Input: " + input.toUpperCase());
        return input.toUpperCase();
    }

    @Override
    public String unmarshal(@Nullable Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

    @Override
    public Object marshal(String o) {
        return o;
    }
}