package android.cesi.todo.helper;

import android.app.Activity;
import android.cesi.todo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Tools {

    private static SimpleDateFormat LDF = null;

    public static SimpleDateFormat getListDateFormat() {
        return LDF;
    }

    public static void setListDateFormat(String pattern) {
        LDF = new SimpleDateFormat(pattern);
    }

    public static Toast getCustomToast(Context context, int text, int length) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) ((Activity) context).findViewById(R.id.toast_custom));

        Toast toast = new Toast(context);
        toast.setDuration(length);
        toast.setView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.toast_content);
        textView.setText(text);
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorAccent);
        toast.setView(view);

        return toast;
    }

    public static Toast getCustomToast(Context context, String text, int length) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) ((Activity) context).findViewById(R.id.toast_custom));

        Toast toast = new Toast(context);
        toast.setDuration(length);
        toast.setView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.toast_content);
        textView.setText(text);
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorAccent);
        toast.setView(view);

        return toast;
    }

}
