import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.document.Document;

// import javax.naming.directory.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;

public class JsonTester {

    public static void main(String[] args) throws IOException, ParseException {

        String indexDir = "/Users/farisnajid/Documents/MSFT/MH6301 - Information Retrieval & Analysis/java codes/index";
        String dataDir = "/Users/farisnajid/Documents/MSFT/MH6301 - Information Retrieval & Analysis/java codes/yelp_dataset";

        // Create a JsonIndexer object and index the JSON files
        JsonIndexer indexer = new JsonIndexer(indexDir);
        indexer.indexJsonFiles(dataDir);

        // Create a JsonSearcher object
        JsonSearcher searcher = new JsonSearcher(indexDir);

        // Create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Loop to keep asking for queries until user types "exit"
        while (true) {

            System.out.println("What type of Query? (enter \"b\" for Boolean query, \"r\" for Ranking query, or \"exit\" to quit):");
            String queryType = scanner.nextLine();
            boolean isRankingQuery = queryType.equals("r") ? true: false;

            if (queryType.equals("exit")) {
                break;
            }

            System.out.println("Enter the query (type \"exit\" to quit):");
            String query = scanner.nextLine();

            if (query.equals("exit")) {
                break;
            }

            // Perform the search and print the results
            List<Document> results = searcher.search(query, isRankingQuery, 20);
            int resultSize = results.size();

            if (resultSize == 0) {
                System.out.println("No results found.");
            } else {
                for (Document result : results) {
                    System.out.println("Total results: " + resultSize);
                    System.out.println("{" + " review_id: " + result.get("review_id") + ", user_id: " + result.get("user_id") + ", stars: " + result.get("stars") + ", useful: " + result.get("useful") + ", funny: " + result.get("funny") + ", cool: " + result.get("cool") + ", date: " + result.get("date") + ", text: " + result.get("text") + " }");
                }
            }
        }

        // Close the scanner
        scanner.close();
    }
}

