import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Database2 {

    // 材料を表すクラス
    static class Ingredient {
        private String name; // 材料名
        private double amount; // 材料の量 (グラム)

        public Ingredient(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public double getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return name + ": " + amount + "g";
        }
    }

    // レシピを表すクラス
    static class Recipe {
        private String menu; // レシピ名
        private int timeSeconds; // 調理時間 (秒)
        private List<Ingredient> ingredients; // 材料のリスト

        public Recipe(String menu, int timeSeconds, List<Ingredient> ingredients) {
            this.menu = menu;
            this.timeSeconds = timeSeconds;
            this.ingredients = ingredients;
        }

        public String getMenu() {
            return menu;
        }

        public int getTimeSeconds() {
            return timeSeconds;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Menu: ").append(menu).append("\n");
            sb.append("Time (seconds): ").append(timeSeconds).append("\n");
            sb.append("Ingredients:\n");
            for (Ingredient ingredient : ingredients) {
                sb.append(" - ").append(ingredient).append("\n");
            }
            return sb.toString();
        }
    }

    // Database2: レシピを格納するリスト
    private static List<Recipe> Database2 = new ArrayList<>();

    /**
     * JSONデータからDatabase2を構築するメソッド
     * @param jsonData JSONデータ
     */
    public static void createDatabase2(String jsonData) {
        // JSONデータを解析
        JSONArray recipes = new JSONArray(jsonData);

        // 各レシピを処理
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipeObj = recipes.getJSONObject(i);

            // レシピ情報を抽出
            String menu = recipeObj.getString("menu");
            int timeSeconds = recipeObj.getInt("time_seconds");

            // 材料を抽出
            List<Ingredient> ingredientList = new ArrayList<>();
            JSONArray ingredients = recipeObj.getJSONArray("ingredients");
            for (int j = 0; j < ingredients.length(); j++) {
                JSONObject ingredientObj = ingredients.getJSONObject(j);
                String name = ingredientObj.getString("name");
                double amount = ingredientObj.getDouble("amount");
                ingredientList.add(new Ingredient(name, amount));
            }

            // レシピを作成しDatabase2に追加
            Recipe recipe = new Recipe(menu, timeSeconds, ingredientList);
            Database2.add(recipe);
        }
    }

    /**
     * Database2を表示するメソッド
     */
    public static void displayDatabase2() {
        System.out.println("Database2:");
        for (Recipe recipe : Database2) {
            System.out.println(recipe);
        }
    }

    /**
     * デバッグ用のメインメソッド
     */
    public static void main(String[] args) {
        // JSONデータのモック
        String jsonData = """
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

        // Database2を作成
        createDatabase2(jsonData);

        // Database2を表示
        displayDatabase2();
    }
}