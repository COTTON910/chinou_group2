import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Database2 {

    // �ޗ���\���N���X
    static class Ingredient {
        private String name; // �ޗ���
        private double amount; // �ޗ��̗� (�O����)

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

    // ���V�s��\���N���X
    static class Recipe {
        private String menu; // ���V�s��
        private int timeSeconds; // �������� (�b)
        private List<Ingredient> ingredients; // �ޗ��̃��X�g

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

    // Database2: ���V�s���i�[���郊�X�g
    private static List<Recipe> Database2 = new ArrayList<>();

    /**
     * JSON�f�[�^����Database2���\�z���郁�\�b�h
     * @param jsonData JSON�f�[�^
     */
    public static void createDatabase2(String jsonData) {
        // JSON�f�[�^�����
        JSONArray recipes = new JSONArray(jsonData);

        // �e���V�s������
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipeObj = recipes.getJSONObject(i);

            // ���V�s���𒊏o
            String menu = recipeObj.getString("menu");
            int timeSeconds = recipeObj.getInt("time_seconds");

            // �ޗ��𒊏o
            List<Ingredient> ingredientList = new ArrayList<>();
            JSONArray ingredients = recipeObj.getJSONArray("ingredients");
            for (int j = 0; j < ingredients.length(); j++) {
                JSONObject ingredientObj = ingredients.getJSONObject(j);
                String name = ingredientObj.getString("name");
                double amount = ingredientObj.getDouble("amount");
                ingredientList.add(new Ingredient(name, amount));
            }

            // ���V�s���쐬��Database2�ɒǉ�
            Recipe recipe = new Recipe(menu, timeSeconds, ingredientList);
            Database2.add(recipe);
        }
    }

    /**
     * Database2��\�����郁�\�b�h
     */
    public static void displayDatabase2() {
        System.out.println("Database2:");
        for (Recipe recipe : Database2) {
            System.out.println(recipe);
        }
    }

    /**
     * �f�o�b�O�p�̃��C�����\�b�h
     */
    public static void main(String[] args) {
        // JSON�f�[�^�̃��b�N
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

        // Database2���쐬
        createDatabase2(jsonData);

        // Database2��\��
        displayDatabase2();
    }
}