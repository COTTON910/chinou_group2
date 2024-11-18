/* 2024-11-14ver 後で消す予定
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTRecipeFinder {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY"); // OpenAI APIキーをここに入力
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static JSONArray getRecipesFromIngredients(List<String> ingredients) throws Exception {
        // プロンプトを作成
        StringBuilder prompt = new StringBuilder("Given the following ingredients: ");
        for (int i = 0; i < ingredients.size(); i++) {
            prompt.append(ingredients.get(i));
            if (i < ingredients.size() - 1) prompt.append(", ");
        }
        prompt.append(", provide a list of possible recipes. For each recipe, return:\n")
              .append("- The name of the recipe (string)\n")
              .append("- Ingredients used in the recipe (string)\n")
              .append("- Amount of each ingredient in floating point (float)\n")
              .append("- Cooking time in seconds (int)\n\n")
              .append("Output the results as a JSON format where each recipe is a dictionary with the following structure:\n")
              .append("{ \"menu\": \"Recipe name\", \"ingredients\": [{\"name\": \"ingredient name\", \"amount\": float}], \"time_seconds\": int }");

        // APIリクエストの設定
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // リクエストボディを作成
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("prompt", prompt.toString());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.5);

        // リクエスト送信
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // レスポンスの読み取り
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // JSONデータを解析
        JSONObject jsonResponse = new JSONObject(response.toString());
        String textResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text").trim();
        return new JSONArray(textResponse);
    }
    
    private static List<String[]> Database1 = new ArrayList<>();

    public static void main(String[] args) {
        try {
        	
            // 材料リストの作成
            List<String> ingredients = new ArrayList<>();
            ingredients.add("tomato");
            ingredients.add("cheese");
            ingredients.add("basil");
            ingredients.add("olive oil");

            // レシピを取得
            JSONArray recipes = getRecipesFromIngredients(ingredients);

            // 結果の表示
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipe = recipes.getJSONObject(i);
                System.out.println("Menu: " + recipe.getString("menu"));
                System.out.println("Time (seconds): " + recipe.getInt("time_seconds"));
                JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject ingredient = ingredientsArray.getJSONObject(j);
                    System.out.println(" - Ingredient: " + ingredient.getString("name") + ", Amount: " + ingredient.getDouble("amount"));
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTRecipeFinder {

    // Database1: 「材料の名前、材料の量、材料の期限」の順にデータを格納
    private static List<String[]> Database1 = new ArrayList<>();

    /**
     * Database1の材料を基にレシピを取得し、結果をリストとして返すメソッド
     * @return レシピのリスト (List<JSONObject>)
     * @throws Exception APIの通信エラー時
     */
    public static List<JSONObject> getRecipesFromDatabase() throws Exception {
        // Database1から材料名を抽出
        List<String> ingredients = getIngredientNames();

        // ChatGPT APIを使用してレシピを取得
        JSONArray recipes = getRecipesFromIngredients(ingredients);

        // レシピをリストに変換
        List<JSONObject> recipeList = new ArrayList<>();
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipe = recipes.getJSONObject(i);
            recipeList.add(recipe);
        }

        return recipeList;
    }

    /**
     * Database1から材料名（最初の要素）を抽出するメソッド
     * @return 材料名のリスト
     */
    private static List<String> getIngredientNames() {
        List<String> names = new ArrayList<>();
        for (String[] entry : Database1) {
            if (entry.length > 0) { // 安全のため長さチェック
                names.add(entry[0]); // 材料名（配列の最初の要素）をリストに追加
            }
        }
        return names;
    }
/*
    /**
     * 指定した材料リストを基にChatGPT APIを使用してレシピを取得するメソッド
     * @param ingredients 材料名のリスト
     * @return レシピ情報のJSONArray
     * @throws Exception APIの通信エラー時
     */
    private static JSONArray getRecipesFromIngredients(List<String> ingredients) throws Exception {
    	/*
        // プロンプトを作成
        StringBuilder prompt = new StringBuilder("Given the following ingredients: ");
        for (int i = 0; i < ingredients.size(); i++) {
            prompt.append(ingredients.get(i));
            if (i < ingredients.size() - 1) prompt.append(", ");
        }
        prompt.append(", provide a list of possible recipes. For each recipe, return:\n")
        .append("- The name of the recipe (string)\n")
        .append("- Ingredients used in the recipe (string)\n")
        .append("- Amount of each ingredient in grams (float, always in grams)\n") // 単位をグラムに限定
        .append("- Cooking time in seconds (int)\n\n")
        .append("Output the results as a JSON format where each recipe is a dictionary with the following structure:\n")
        .append("{ \"menu\": \"Recipe name\", \"ingredients\": [{\"name\": \"ingredient name\", \"amount\": float}], \"time_seconds\": int }")
        .append("\nAll ingredient amounts must be in grams.");

        // API URL
        String API_URL = "https://api.openai.com/v1/completions";

        // HTTPリクエストの設定
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // リクエストボディを作成
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("prompt", prompt.toString());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.5);

        // リクエストを送信
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // レスポンスを受け取る
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // レスポンスのJSON解析
        JSONObject jsonResponse = new JSONObject(response.toString());
        String textResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text").trim();
        return new JSONArray(textResponse);
        */
        return new JSONArray(getMockResponse());
    }
    
    private static JSONArray getMockResponse() {
        String mockResponse = """
        [
          {
            "menu": "Caprese Salad",
            "time_seconds": 600,
            "ingredients": [
              {"name": "tomato", "amount": 200.0},
              {"name": "cheese", "amount": 100.0},
              {"name": "basil", "amount": 50.0},
              {"name": "olive oil", "amount": 10.0}
            ]
          }
        ]
        """;
        return new JSONArray(mockResponse);
    }
	/**
     * デバッグ用のメインメソッド
     */
    public static void main(String[] args) {
        try {
            // Database1にデータを登録
            Database1.add(new String[]{"tomato", "5", "2024-12-31"});
            Database1.add(new String[]{"cheese", "2", "2024-11-30"});
            Database1.add(new String[]{"basil", "10", "2024-11-20"});
            Database1.add(new String[]{"olive oil", "1", "2025-01-15"});

            // レシピを取得
            List<JSONObject> recipes = getRecipesFromDatabase();

            // 結果の表示
            System.out.println("Recipes:");
            for (JSONObject recipe : recipes) {
                System.out.println("Menu: " + recipe.getString("menu"));
                System.out.println("Time (seconds): " + recipe.getInt("time_seconds"));
                JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject ingredient = ingredientsArray.getJSONObject(j);
                    System.out.println(" - Ingredient: " + ingredient.getString("name") + ", Amount: " + ingredient.getDouble("amount"));
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

