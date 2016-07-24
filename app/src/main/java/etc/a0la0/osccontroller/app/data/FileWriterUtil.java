package etc.a0la0.osccontroller.app.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import etc.a0la0.osccontroller.app.data.entities.Option;

/**
 * Created by lukeanderson on 7/10/16.
 */
public class FileWriterUtil {

    private static Gson gson = new Gson();
    private static Type listType = new TypeToken<ArrayList<Option>>(){}.getType();
    private static String filePath = "data.txt";

    public static void storeModel(Context context, List<Option> optionList) {
        String jsonString = gson.toJson(optionList);
        writeToFile(context, jsonString);
    }

    public static List<Option> getModelFromStore(Context context) {
        String jsonString = readFromFile(context);
        if (jsonString.equals("") || jsonString == null) {
            jsonString = "[]";
        }
        return gson.fromJson(jsonString, listType);
    }

    private static void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("WriteToFile Exception:", e.toString());
            e.printStackTrace();
        }
    }

    private static String readFromFile(Context context) {
        String fileContent = "";
        try {
            InputStream inputStream = context.openFileInput(filePath);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                fileContent = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return fileContent;
    }

    public static void deleteFile() {
        File file = new File(filePath);
        boolean deleted = file.delete();
        Log.i("deleted file", deleted + "");
    }

}
