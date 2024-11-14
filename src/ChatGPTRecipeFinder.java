import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTRecipeFinder {

	private static final String API_KEY = System.getenv("OPENAI_API_KEY"); // OpenAI API�L�[�������ɓ���
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static JSONArray getRecipesFromIngredients(String[] ingredients) throws Exception {
        // �v�����v�g���쐬
        StringBuilder prompt = new StringBuilder("Given the following ingredients: ");
        for (int i = 0; i < ingredients.length; i++) {
            prompt.append(ingredients[i]);
            if (i < ingredients.length - 1) prompt.append(", ");
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

    public static void main(String[] args) {
        try {
            String[] ingredients = {"tomato", "cheese", "basil", "olive oil"};
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
