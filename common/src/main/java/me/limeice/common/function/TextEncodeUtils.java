package me.limeice.common.function;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;

/**
 * 用于处理文本
 *
 * <pre>
 *     author: LimeVista(Lime)
 *     time  : 2018/11/30
 *     desc  : 用于处理文本
 *     github: https://github.com/LimeVista/EasyCommon
 * </pre>
 */
public final class TextEncodeUtils {

    private TextEncodeUtils() {
        throw new AssertionError("No TextEncodeUtils instances for you!");
    }

    /**
     * Return the string of decode html-encode string.
     *
     * @param input The input.
     * @return the string of decode html-encode string
     */
    @SuppressWarnings("deprecation")
    @NonNull
    public static CharSequence string2Html(@NonNull final String input) {
        Objects.requireNonNull(input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
}
