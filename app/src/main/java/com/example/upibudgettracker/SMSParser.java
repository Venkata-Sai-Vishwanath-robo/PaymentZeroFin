package  com.example.upibudgettracker;
import android.content.Context; import android.database.Cursor; import android.net.Uri; import android.text.format.DateUtils;


import java.util.ArrayList; import java.util.Calendar; import java.util.List; import java.util.regex.Matcher; import java.util.regex.Pattern;

public class SMSParser { private static final Pattern AMOUNT_PATTERN = Pattern.compile("(?i)(?:inr|rs\\.?|₹)\\s*([\\d,]+(?:\\.\\d{1,2})?)");

    public static float getMonthlySpend(Context context) {
        float total = 0;
        List<String> messages = getUPIMessages(context);
        for (String msg : messages) {
            Matcher matcher = AMOUNT_PATTERN.matcher(msg);
            if (matcher.find()) {
                String amtStr = matcher.group(1).replace(",", "");
                try {
                    total += Float.parseFloat(amtStr);
                } catch (NumberFormatException ignored) {}
            }
        }
        return total;
    }

    // TODO - The phone pe wallet messages show the spent amount and the balance as well, we need to handle those cases as well
    // Reading from bank messages works because they don't show the balance
    public static List<String> getUPIMessages(Context context) {
        List<String> messages = new ArrayList<>();
        Uri uriSms = Uri.parse("content://sms/inbox");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long startOfMonth = cal.getTimeInMillis();

        try (Cursor cursor = context.getContentResolver().query(uriSms, null, "date >= ?", new String[]{String.valueOf(startOfMonth)}, "date DESC")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    if ((body.toLowerCase().contains("upi") || body.toLowerCase().contains("phonepe wallet")) && (body.toLowerCase().contains("sent") || body.toLowerCase().contains("paid"))) {
                        messages.add(body);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            messages.add("Error reading SMS: " + e.getMessage());
        }
        return messages;
    }

}

