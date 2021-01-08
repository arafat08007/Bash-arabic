package app.bash.utilitis;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;

        int day = (int) TimeUnit.SECONDS.toDays(TimeUnit.MILLISECONDS.toSeconds(diff));
        long hours = TimeUnit.SECONDS.toHours(TimeUnit.MILLISECONDS.toSeconds(diff))

                - TimeUnit.DAYS.toHours(day);
        int months = day / 30;
        int years = months / 12;
        long minute = TimeUnit.SECONDS.toMinutes(TimeUnit.MILLISECONDS.toSeconds(diff))
                - TimeUnit.DAYS.toMinutes(day)
                - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(TimeUnit.MILLISECONDS.toSeconds(diff))
                - TimeUnit.DAYS.toSeconds(day)
                - TimeUnit.HOURS.toSeconds(hours)
                - TimeUnit.MINUTES.toSeconds(minute);
        String postTime = "";
        if (years > 0) {
            if (TextUtils.isEmpty(postTime)) {
                if (years == 1) {
                    postTime = years + " year";
                } else {
                    postTime = years + " years";
                }

            } else {
                postTime = postTime + " " + years + " years";
            }
        } else if (months > 0) {
            if (TextUtils.isEmpty(postTime)) {
                if (months == 1) {
                    postTime = months + " month";
                } else {
                    postTime = months + " months";
                }
            } else
                postTime = postTime + " " + months + " months";
        } else {
            if (day == 0) {
                postTime = "Today";
            } else if (day == 1) {
                postTime = day + " day";
            } else {
                postTime = day + " days";
            }
        }/*else if (day > 0) {
            if (day == 1) {
                postTime = day + " day";
            } else {
                postTime = day + " days";
            }
        } else if (hours > 0) {
            if (TextUtils.isEmpty(postTime)) {
                if (hours == 1) {
                    postTime = hours + " hour";
                } else {
                    postTime = hours + " hours";
                }

            } else {
                postTime = postTime + " " + hours + " hours";
            }

        } else if (minute > 0) {
            if (TextUtils.isEmpty(postTime)) {
                if (minute == 1) {
                    postTime = minute + " min";
                } else {
                    postTime = minute + " mins";
                }
            } else
                postTime = postTime + " " + minute + " mins";
        } else {
            if (TextUtils.isEmpty(postTime))
                postTime = "second";
        }*/
        if (postTime.equals("Today")) {
            postTime = "Today";
        } else {
            postTime = postTime + " ago";
        }

        System.out.println(" years " + years + " months " + months + " Day " + day + " Hour " + hours + " Minute "
                + minute + " Seconds " + second);
        return postTime;

        /*if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }*/
    }

}
