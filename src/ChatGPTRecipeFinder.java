/* 2024-11-14ver ��ŏ����\��
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

    private static final String API_KEY = System.getenv("OPENAI_API_KEY"); // OpenAI API�L�[�������ɓ���
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static JSONArray getRecipesFromIngredients(List<String> ingredients) throws Exception {
        // �v�����v�g���쐬
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

        // API���N�G�X�g�̐ݒ�
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // ���N�G�X�g�{�f�B���쐬
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("prompt", prompt.toString());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.5);

        // ���N�G�X�g���M
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // ���X�|���X�̓ǂݎ��
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // JSON�f�[�^�����
        JSONObject jsonResponse = new JSONObject(response.toString());
        String textResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text").trim();
        return new JSONArray(textResponse);
    }
    
    private static List<String[]> Database1 = new ArrayList<>();

    public static void main(String[] args) {
        try {
        	
            // �ޗ����X�g�̍쐬
            List<String> ingredients = new ArrayList<>();
            ingredients.add("tomato");
            ingredients.add("cheese");
            ingredients.add("basil");
            ingredients.add("olive oil");

            // ���V�s���擾
            JSONArray recipes = getRecipesFromIngredients(ingredients);

            // ���ʂ̕\��
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

    // Database1: �u�ޗ��̖��O�A�ޗ��̗ʁA�ޗ��̊����v�̏��Ƀf�[�^���i�[
    private static List<String[]> Database1 = new ArrayList<>();

    /**
     * Database1�̍ޗ�����Ƀ��V�s���擾���A���ʂ����X�g�Ƃ��ĕԂ����\�b�h
     * @return ���V�s�̃��X�g (List<JSONObject>)
     * @throws Exception API�̒ʐM�G���[��
     */
    public static List<JSONObject> getRecipesFromDatabase() throws Exception {
        // Database1����ޗ����𒊏o
        List<String> ingredients = getIngredientNames();

        // ChatGPT API���g�p���ă��V�s���擾
        JSONArray recipes = getRecipesFromIngredients(ingredients);

        // ���V�s�����X�g�ɕϊ�
        List<JSONObject> recipeList = new ArrayList<>();
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipe = recipes.getJSONObject(i);
            recipeList.add(recipe);
        }

        return recipeList;
    }

    /**
     * Database1����ޗ����i�ŏ��̗v�f�j�𒊏o���郁�\�b�h
     * @return �ޗ����̃��X�g
     */
    private static List<String> getIngredientNames() {
        List<String> names = new ArrayList<>();
        for (String[] entry : Database1) {
            if (entry.length > 0) { // ���S�̂��ߒ����`�F�b�N
                names.add(entry[0]); // �ޗ����i�z��̍ŏ��̗v�f�j�����X�g�ɒǉ�
            }
        }
        return names;
    }
/*
    /**
     * �w�肵���ޗ����X�g�����ChatGPT API���g�p���ă��V�s���擾���郁�\�b�h
     * @param ingredients �ޗ����̃��X�g
     * @return ���V�s����JSONArray
     * @throws Exception API�̒ʐM�G���[��
     */
    private static JSONArray getRecipesFromIngredients(List<String> ingredients) throws Exception {
    	/*
        // �v�����v�g���쐬
        StringBuilder prompt = new StringBuilder("Given the following ingredients: ");
        for (int i = 0; i < ingredients.size(); i++) {
            prompt.append(ingredients.get(i));
            if (i < ingredients.size() - 1) prompt.append(", ");
        }
        prompt.append(", provide a list of possible recipes. For each recipe, return:\n")
        .append("- The name of the recipe (string)\n")
        .append("- Ingredients used in the recipe (string)\n")
        .append("- Amount of each ingredient in grams (float, always in grams)\n") // �P�ʂ��O�����Ɍ���
        .append("- Cooking time in seconds (int)\n\n")
        .append("Output the results as a JSON format where each recipe is a dictionary with the following structure:\n")
        .append("{ \"menu\": \"Recipe name\", \"ingredients\": [{\"name\": \"ingredient name\", \"amount\": float}], \"time_seconds\": int }")
        .append("\nAll ingredient amounts must be in grams.");

        // API URL
        String API_URL = "https://api.openai.com/v1/completions";

        // HTTP���N�G�X�g�̐ݒ�
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // ���N�G�X�g�{�f�B���쐬
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("prompt", prompt.toString());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.5);

        // ���N�G�X�g�𑗐M
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // ���X�|���X���󂯎��
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // ���X�|���X��JSON���
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
     * �f�o�b�O�p�̃��C�����\�b�h
     */
    public static void main(String[] args) {
        try {
            // Database1�Ƀf�[�^��o�^
            Database1.add(new String[]{"tomato", "5", "2024-12-31"});
            Database1.add(new String[]{"cheese", "2", "2024-11-30"});
            Database1.add(new String[]{"basil", "10", "2024-11-20"});
            Database1.add(new String[]{"olive oil", "1", "2025-01-15"});

            // ���V�s���擾
            List<JSONObject> recipes = getRecipesFromDatabase();

            // ���ʂ̕\��
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

