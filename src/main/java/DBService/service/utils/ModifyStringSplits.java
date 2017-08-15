package DBService.service.utils;

/**
 * Created by Nik on 24.07.2017.
 */
public class ModifyStringSplits {
    public static String modify(String input) {
        String output = "";

        if (input.contains("_")) {
            String[] sub = input.split("_");
            for (int i = 0; i < sub.length - 1; i++)
                output += sub[i] + " ";

            output += sub[sub.length - 1];
        }

        return output.isEmpty() ? input : output;
    }
}
